package com.costostudio.ninao.presentation.uistate

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val confirmPassword: String = "",
    val isConfirmPasswordVisible: Boolean = false,
    val baseScreenUiState: BaseScreenUiState = BaseScreenUiState()
)
