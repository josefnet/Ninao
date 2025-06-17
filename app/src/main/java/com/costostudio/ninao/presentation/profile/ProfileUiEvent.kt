package com.costostudio.ninao.presentation.profile

sealed class ProfileUiEvent {
    data class FirstNameChanged(val firstName: String): ProfileUiEvent()
    data class LastNameChanged(val lastName: String): ProfileUiEvent()
    data class EmailChanged(val email: String): ProfileUiEvent()
    data class PasswordChanged(val password: String): ProfileUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String): ProfileUiEvent()
    object TogglePasswordVisibility : ProfileUiEvent()
    object ToggleConfirmPasswordVisibility : ProfileUiEvent()
    object SaveModification : ProfileUiEvent()
    object ClearError : ProfileUiEvent()
    object ClearSuccess : ProfileUiEvent()
}