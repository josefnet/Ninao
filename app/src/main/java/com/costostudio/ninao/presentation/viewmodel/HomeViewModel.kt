package com.costostudio.ninao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Fonction pour se déconnecter
    fun logout() {
        auth.signOut()
    }
}