package com.costostudio.ninao.presentation.uistate

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)