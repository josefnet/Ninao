package com.costostudio.ninao.presentation.events

sealed class AuthenticationUiEvent {
    object Success : AuthenticationUiEvent()
    data class ShowError(val message: String) : AuthenticationUiEvent()
}