package com.costostudio.ninao.presentation.register

import com.costostudio.ninao.presentation.util.uistate.BaseScreenUiState

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