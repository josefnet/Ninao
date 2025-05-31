package com.costostudio.ninao.presentation.uistate

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val baseScreenUiState: BaseScreenUiState = BaseScreenUiState()
)
