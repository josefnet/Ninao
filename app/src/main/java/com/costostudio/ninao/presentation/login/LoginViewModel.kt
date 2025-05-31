package com.costostudio.ninao.presentation.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costostudio.ninao.domain.usecase.LoginUseCase
import com.costostudio.ninao.presentation.util.events.AuthenticationUiEvent
import com.costostudio.ninao.util.execute
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
            val message = "Invalid email or password"
            updateLoginUiState(false, message)
            viewModelScope.launch {
                _loginUiEvent.emit(AuthenticationUiEvent.ShowError(message))
            }

            return
        }

        viewModelScope.execute(
            function = {
                loginUseCase.execute(email, password)
            },
            onSuccess = { result ->
                result
                    .onSuccess {
                        updateLoginUiState(false)
                        _loginUiEvent.emit(AuthenticationUiEvent.Success)
                    }
                    .onFailure {
                        updateLoginUiState(false, it.message)
                        _loginUiEvent.emit(
                            AuthenticationUiEvent.ShowError(
                                it.message ?: "Unknown error"
                            )
                        )
                    }
            },
            onError = {
                updateLoginUiState(false, it.message)
                _loginUiEvent.emit(AuthenticationUiEvent.ShowError(it.message ?: "Unknown error"))
            }
        )

    }

    private fun updateLoginUiState(loadingValue: Boolean, errorMessage: String? = null) {
        _loginUiState.update {
            it.copy(
                baseScreenUiState = it.baseScreenUiState.copy(
                    isLoading = loadingValue,
                    errorMessage = errorMessage
                )
            )
        }
    }


    private fun isValidInput(email: String, password: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 6
    }


}