package com.costostudio.ninao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<Boolean?>(null)
    val registerState: StateFlow<Boolean?> = _registerState

    fun register(name: String, email: String, password: String) {
        // TODO: Ajouter logique d'inscription
        _registerState.value = true
    }
}