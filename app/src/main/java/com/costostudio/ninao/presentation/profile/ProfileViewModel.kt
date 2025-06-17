package com.costostudio.ninao.presentation.profile

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costostudio.ninao.domain.model.UserInfo
import com.costostudio.ninao.domain.usecase.GetUserUseCase
import com.costostudio.ninao.domain.usecase.UpdateUserToFireStoreUseCase
import com.costostudio.ninao.util.execute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserToFireStoreUseCase: UpdateUserToFireStoreUseCase,
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

    private fun getUserInfos(userInfo: UserInfo){
        _profileUiState.update {
            it.copy(
                firstName = userInfo.firstName,
                lastName = userInfo.lastName,
                //email = it.email
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

}