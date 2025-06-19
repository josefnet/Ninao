package com.costostudio.ninao.presentation.profile

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costostudio.ninao.domain.model.UserInfo
import com.costostudio.ninao.domain.usecase.GetUserUseCase
import com.costostudio.ninao.domain.usecase.UpdateUserToFireStoreUseCase
import com.costostudio.ninao.domain.usecase.image.CaptureImageFromCameraUseCase
import com.costostudio.ninao.domain.usecase.image.SelectImageFromGalleryUseCase
import com.costostudio.ninao.domain.util.CustomResource
import com.costostudio.ninao.util.execute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserToFireStoreUseCase: UpdateUserToFireStoreUseCase,
    private val selectImageFromGalleryUseCase: SelectImageFromGalleryUseCase,
    private val captureImageFromCameraUseCase: CaptureImageFromCameraUseCase
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    init {
        viewModelScope.execute(
            function = {
                getUserUseCase.execute()
            },

            onSuccess = { result ->
                result
                    .onSuccess { userInfo ->
                        getUserInfos(userInfo)
                        // _profileUiState.update { it.copy(isSuccess = true) }
                    }
                    .onFailure {
                        updateProfileUiState(false, it.message)
                    }
            },
            onError = {
                Log.e("ProfileViewModel", "Error fetching user data: $it")
            }
        )
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.FirstNameChanged -> {
                _profileUiState.update { it.copy(firstName = event.firstName) }
            }

            is ProfileUiEvent.LastNameChanged -> {
                _profileUiState.update { it.copy(lastName = event.lastName) }
            }

            is ProfileUiEvent.EmailChanged -> {
                _profileUiState.update { it.copy(email = event.email) }
            }

            is ProfileUiEvent.PasswordChanged -> {
                _profileUiState.update { it.copy(password = event.password) }
            }

            is ProfileUiEvent.ConfirmPasswordChanged -> {
                _profileUiState.update { it.copy(confirmPassword = event.confirmPassword) }
            }

            ProfileUiEvent.TogglePasswordVisibility -> {
                _profileUiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            ProfileUiEvent.ToggleConfirmPasswordVisibility -> {
                _profileUiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            ProfileUiEvent.SaveModification -> {
                saveModification()
            }

            ProfileUiEvent.ClearError -> _profileUiState.update { it.copy(errorMessage = null) }
            ProfileUiEvent.ClearSuccess -> _profileUiState.update { it.copy(isSuccess = false) }

            ProfileUiEvent.Image.OnImageClicked -> {
                _profileUiState.update {
                    it.copy(
                        imageUiState = it.imageUiState.copy(
                            showImageSourceDialog = true
                        )
                    )
                }
            }

            ProfileUiEvent.Image.OnImageSourceDialogDismissed -> {
                _profileUiState.update {
                    it.copy(
                        imageUiState = it.imageUiState.copy(
                            showImageSourceDialog = false
                        )
                    )
                }
            }

            ProfileUiEvent.Image.OnGallerySelected -> {
                _profileUiState.update {
                    it.copy(
                        imageUiState = it.imageUiState.copy(
                            showImageSourceDialog = false
                        )
                    )
                }
            }

            ProfileUiEvent.Image.OnCameraSelected -> {
                _profileUiState.update {
                    it.copy(
                        imageUiState = it.imageUiState.copy(
                            showImageSourceDialog = false
                        )
                    )
                }
            }

            is ProfileUiEvent.Image.OnImageSelected -> {
                handleImageSelection(event.uri)
            }

            is ProfileUiEvent.Image.OnImageCaptured -> {
                handleImageCapture(event.uri)
            }
        }
    }

    private fun saveModification() {

        val firstName = _profileUiState.value.firstName
        val lastName = _profileUiState.value.lastName
        val email = _profileUiState.value.email
        val password = _profileUiState.value.password
        val confirmPassword = _profileUiState.value.confirmPassword

        if (!isValidInput(firstName, lastName, email, password, confirmPassword)) {
            val message =
                "Vérifiez les champs : email invalide, mot de passe trop court ou non identique."
            viewModelScope.launch {
                _profileUiState.update { it.copy(errorMessage = message) }
            }
            return
        }

        viewModelScope.execute(
            function = {
                updateProfileUiState(true)
                //val uid = firebaseAuth.currentUser?.uid
                updateUserToFireStoreUseCase.execute(
                    "uid.toString()",
                    firstName,
                    lastName,
                    email
                )
            },
            onSuccess = { result ->
                result
                    .onSuccess {
                        updateProfileUiState(false)
                        _profileUiState.update { it.copy(isSuccess = true) }
                    }
                    .onFailure {
                        updateProfileUiState(false, it.message)
                    }
            },
            onError = {
                _profileUiState.update { it.copy(errorMessage = "Unknowing error") }
            }
        )
    }

    private fun updateProfileUiState(loadingValue: Boolean, errorMessage: String? = null) {
        _profileUiState.update { it.copy(isLoading = loadingValue, errorMessage = errorMessage) }
    }

    private fun getUserInfos(userInfo: UserInfo) {
        _profileUiState.update {
            it.copy(
                firstName = userInfo.firstName,
                lastName = userInfo.lastName,
                email = userInfo.email
            )
        }
    }


    private fun isValidInput(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return firstName.isNotBlank()
                && lastName.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
                && confirmPassword.isNotBlank()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 6
                && password == confirmPassword
    }

    private fun handleImageSelection(uri: android.net.Uri) {
        viewModelScope.launch {
            selectImageFromGalleryUseCase(uri).collect { resource ->
                when (resource) {
                    is CustomResource.Loading -> {
                        _profileUiState.update {
                            it.copy(
                                imageUiState = it.imageUiState.copy(
                                    isLoading = true, error = null
                                )
                            )
                        }
                    }

                    is CustomResource.Success -> {
                        _profileUiState.update {
                            it.copy(
                                imageUiState = it.imageUiState.copy(
                                    imageUrl = resource.data,
                                    isLoading = false,
                                    error = null
                                )
                            )
                        }
                    }

                    is CustomResource.Error -> {
                        _profileUiState.update {
                            it.copy(
                                imageUiState = it.imageUiState.copy(
                                    isLoading = false,
                                    error = resource.message
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleImageCapture(uri: android.net.Uri) {
        viewModelScope.launch {
            captureImageFromCameraUseCase(uri).collect { resource ->
                when (resource) {
                    is CustomResource.Loading -> {
                        _profileUiState.update {
                            it.copy(
                                imageUiState = it.imageUiState.copy(
                                    isLoading = true,
                                    error = null
                                )
                            )
                        }

                    }

                    is CustomResource.Success -> {
                        _profileUiState.update {
                            it.copy(
                                imageUiState = it.imageUiState.copy(
                                    imageUrl = resource.data,
                                    isLoading = false,
                                    error = null
                                )
                            )
                        }
                    }

                    is CustomResource.Error -> {
                        _profileUiState.update {
                            it.copy(
                                imageUiState = it.imageUiState.copy(
                                    isLoading = false,
                                    error = resource.message
                                )
                            )
                        }
                    }
                }
            }
        }
    }

}