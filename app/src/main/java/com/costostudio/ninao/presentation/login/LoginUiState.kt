package com.costostudio.ninao.presentation.login

import com.costostudio.ninao.presentation.util.uistate.BaseScreenUiState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val baseScreenUiState: BaseScreenUiState = BaseScreenUiState()
)