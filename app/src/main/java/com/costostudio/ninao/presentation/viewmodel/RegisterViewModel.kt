package com.costostudio.ninao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _registerState = MutableStateFlow<Result<Boolean>?>(null)
    val registerState: StateFlow<Result<Boolean>?> = _registerState

    fun register(firstName: String, lastName: String, email: String, password: String) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _registerState.value = Result.failure(Exception("Veuillez remplir tous les champs"))
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = task.result?.user?.uid
                        if (userId != null) {
                            saveUserToFirestore(userId, firstName, lastName, email)
                        }
                    } else {
                        _registerState.value = Result.failure(task.exception ?: Exception("Échec de l'inscription"))
                    }
                }
        }
    }

    private fun saveUserToFirestore(userId: String, firstName: String, lastName: String, email: String) {
        val user = hashMapOf(
            "id" to userId,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email
        )

        firestore.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                _registerState.value = Result.success(true)
            }
            .addOnFailureListener { exception ->
                _registerState.value = Result.failure(exception)
            }
    }
}