package com.costostudio.ninao.presentation.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costostudio.ninao.domain.usecase.LoginUseCase
import com.costostudio.ninao.presentation.util.events.AuthenticationUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    private val _loginUiEvent = MutableSharedFlow<AuthenticationUiEvent>()
    val loginUiEvent: SharedFlow<AuthenticationUiEvent> = _loginUiEvent

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _loginUiState.update { it.copy(email = event.email) }
            }

            is LoginEvent.PasswordChanged -> {
                _loginUiState.update { it.copy(password = event.password) }
            }

            LoginEvent.TogglePasswordVisibility -> {
                _loginUiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            LoginEvent.Submit -> {
                login()
            }
        }
    }

    fun login() {

        val email = _loginUiState.value.email
        val password = _loginUiState.value.password

        if (!isValidInput(email, password)) {
            //_loginUiState.value = LoginUiState(errorMessage = "Invalid email or password") // ca on va réinitilisé les autre valeur
            _loginUiState.update {
                it.copy(
                    baseScreenUiState = it.baseScreenUiState.copy(
                        errorMessage = "Invalid email or password"
                    )
                )
            } //ca garde les autre valeur
            return
        }
        viewModelScope.launch {
            _loginUiState.update {
                it.copy(
                    baseScreenUiState = it.baseScreenUiState.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }

            val result = loginUseCase.execute(email, password)
            if (result.isSuccess) {
                _loginUiState.update {
                    it.copy(
                        baseScreenUiState = it.baseScreenUiState.copy(isLoading = false)
                    )
                }
                _loginUiEvent.emit(AuthenticationUiEvent.Success)
            } else {
                val message = result.exceptionOrNull()?.message ?: "Unknown error"
                _loginUiState.update {
                    it.copy(
                        baseScreenUiState = it.baseScreenUiState.copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    )
                }
                _loginUiEvent.emit(AuthenticationUiEvent.ShowError(message))
            }
        }
    }

    private fun isValidInput(email: String, password: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 6
    }


}