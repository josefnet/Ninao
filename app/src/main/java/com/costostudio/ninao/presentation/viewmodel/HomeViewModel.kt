package com.costostudio.ninao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.costostudio.ninao.domain.repository.AuthRepository
import com.costostudio.ninao.presentation.uistate.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        val user = authRepository.getCurrentUser()
        _uiState.value = HomeUiState(
            isLoggedIn = user != null,
            userEmail = user?.email
        )
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = HomeUiState(isLoggedIn = false)
    }
}