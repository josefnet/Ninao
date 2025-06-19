package com.costostudio.ninao.presentation.profile

import android.net.Uri

sealed class ProfileUiEvent {

    // Text field changes
    data class FirstNameChanged(val firstName: String) : ProfileUiEvent()
    data class LastNameChanged(val lastName: String) : ProfileUiEvent()
    data class EmailChanged(val email: String) : ProfileUiEvent()
    data class PasswordChanged(val password: String) : ProfileUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : ProfileUiEvent()

    // Password visibility
    object TogglePasswordVisibility : ProfileUiEvent()
    object ToggleConfirmPasswordVisibility : ProfileUiEvent()

    // Save, clear actions
    object SaveModification : ProfileUiEvent()
    object ClearError : ProfileUiEvent()
    object ClearSuccess : ProfileUiEvent()

    // Nested: image-related events
    sealed class Image : ProfileUiEvent() {
        object OnImageClicked : Image()
        object OnGallerySelected : Image()
        object OnCameraSelected : Image()
        object OnImageSourceDialogDismissed : Image()
        data class OnImageSelected(val uri: Uri) : Image()
        data class OnImageCaptured(val uri: Uri) : Image()
    }
}