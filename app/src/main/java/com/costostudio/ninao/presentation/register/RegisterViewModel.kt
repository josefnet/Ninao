package com.costostudio.ninao.presentation.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costostudio.ninao.domain.usecase.RegisterUseCase
import com.costostudio.ninao.domain.usecase.SaveUserToFireStoreUseCase
import com.costostudio.ninao.presentation.util.events.AuthenticationUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val saveUserToFireStoreUseCase: SaveUserToFireStoreUseCase,
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    private val _registerUiEvent = MutableSharedFlow<AuthenticationUiEvent>()
    val registerUiEvent: SharedFlow<AuthenticationUiEvent> = _registerUiEvent

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FirstNameChanged -> {
                _registerUiState.update { it.copy(firstName = event.firstName) }
            }

            is RegisterEvent.LastNameChanged -> {
                _registerUiState.update { it.copy(lastName = event.lastName) }
            }

            is RegisterEvent.EmailChanged -> {
                _registerUiState.update { it.copy(email = event.email) }
            }

            is RegisterEvent.PasswordChanged -> {
                _registerUiState.update { it.copy(password = event.password) }
            }

            is RegisterEvent.ConfirmPasswordChanged -> {
                _registerUiState.update { it.copy(confirmPassword = event.confirmPassword) }
            }

            RegisterEvent.TogglePasswordVisibility -> {
                _registerUiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            RegisterEvent.ToggleConfirmPasswordVisibility -> {
                _registerUiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            RegisterEvent.Register -> {
                register()
            }

        }
    }


    fun register() {

        val firstName = _registerUiState.value.firstName
        val lastName = _registerUiState.value.lastName
        val email = _registerUiState.value.email
        val password = _registerUiState.value.password
        val confirmPassword = _registerUiState.value.confirmPassword

        if (!isValidInput(firstName, lastName, email, password, confirmPassword)) {
            val message =
                "Vérifiez les champs : email invalide, mot de passe trop court ou non identique."
            _registerUiState.update {
                it.copy(
                    baseScreenUiState = it.baseScreenUiState.copy(
                        errorMessage = message
                    )
                )
            }
            return
        }

        viewModelScope.launch {

            _registerUiState.update {
                it.copy(
                    baseScreenUiState = it.baseScreenUiState.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }

            val result = registerUseCase.execute(firstName, lastName, email, password)
            if (result.isSuccess) {
                _registerUiState.update {
                    it.copy(
                        baseScreenUiState = it.baseScreenUiState.copy(isLoading = false)
                    )
                }

                val authResult = saveUserToFireStoreUseCase.execute(
                    result.getOrNull()!!,
                    firstName,
                    lastName,
                    email
                )
                if (authResult.isSuccess) {
                    _registerUiState.update {
                        it.copy(
                            baseScreenUiState = it.baseScreenUiState.copy(isLoading = false)
                        )
                    }
                    _registerUiEvent.emit(AuthenticationUiEvent.Success)
                } else {
                    val message = authResult.exceptionOrNull()?.message
                        ?: "Erreur lors de l'enregistrement dans Firestore"
                    _registerUiState.update {
                        it.copy(
                            baseScreenUiState = it.baseScreenUiState.copy(
                                isLoading = false,
                                errorMessage = message
                            )
                        )
                    }
                    _registerUiEvent.emit(AuthenticationUiEvent.ShowError(message))
                }
            } else {
                val message = result.exceptionOrNull()?.message ?: "Unknown error"
                _registerUiState.update {
                    it.copy(
                        baseScreenUiState = it.baseScreenUiState.copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    )
                }
                _registerUiEvent.emit(AuthenticationUiEvent.ShowError(message))
            }

        }
    }

    fun isValidInput(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return firstName.isNotBlank()
                && lastName.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
                && confirmPassword.isNotBlank()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 6
                && password == confirmPassword
    }

}