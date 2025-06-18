package com.costostudio.ninao.presentation.profile


data class ProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val confirmPassword: String = "",
    val isConfirmPasswordVisible: Boolean = false,
    val imageUrl: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
    /*val baseScreenUiState: BaseScreenUiState = BaseScreenUiState()*/
)
