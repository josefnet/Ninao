package com.costostudio.ninao.presentation.states

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val userId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}