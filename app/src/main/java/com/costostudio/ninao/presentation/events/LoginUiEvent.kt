package com.costostudio.ninao.presentation.events

sealed class LoginUiEvent {
    object Success : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}