package com.costostudio.ninao.data.repository

import com.costostudio.ninao.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun saveUser(uid: String, firstName: String, lastName: String, email: String): Result<Boolean> {
        return try {
            val user = hashMapOf(
                "id" to uid,
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email
            )
            firestore.collection("users").document(uid).set(user).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}