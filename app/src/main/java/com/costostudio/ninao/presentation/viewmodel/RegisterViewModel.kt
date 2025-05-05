package com.costostudio.ninao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costostudio.ninao.domain.repository.AuthRepository
import com.costostudio.ninao.domain.repository.UserRepository
import com.costostudio.ninao.domain.util.validator.RegisterValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<Result<Boolean>?>(null)
    val registerState: StateFlow<Result<Boolean>?> = _registerState

    fun register(firstName: String, lastName: String, email: String, password: String) {
        val validation = RegisterValidator.validate(firstName, lastName, email, password)
        if (validation.isFailure) {
            _registerState.value = Result.failure(validation.exceptionOrNull()!!)
            return
        }

        viewModelScope.launch {
            val authResult = authRepo.register(email, password)
            if (authResult.isSuccess) {
                val uid = authResult.getOrNull()!!
                val userResult = userRepo.saveUser(uid, firstName, lastName, email)
                _registerState.value = userResult
            } else {
                _registerState.value = Result.failure(authResult.exceptionOrNull()!!)
            }
        }
    }
}