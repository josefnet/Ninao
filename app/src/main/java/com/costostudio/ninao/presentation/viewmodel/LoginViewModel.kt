package com.costostudio.ninao.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costostudio.ninao.domain.usecase.LoginUseCase
import com.costostudio.ninao.presentation.events.LoginUiEvent
import com.costostudio.ninao.presentation.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent

    fun login(email: String, password: String) {

        if (!isValidInput(email, password)) {
            _uiState.value = LoginUiState(errorMessage = "Invalid email or password")
            return
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            val result = loginUseCase.execute(email, password)
            if (result.isSuccess) {
                _uiState.value = LoginUiState()
                _uiEvent.emit(LoginUiEvent.Success)
            } else {
                val message = result.exceptionOrNull()?.message ?: "Unknown error"
                _uiState.value = LoginUiState(errorMessage = message)
                _uiEvent.emit(LoginUiEvent.ShowError(message))
            }
        }
    }

    private fun isValidInput(email: String, password: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 6
    }

}
