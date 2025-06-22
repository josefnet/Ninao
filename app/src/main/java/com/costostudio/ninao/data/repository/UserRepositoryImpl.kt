package com.costostudio.ninao.data.repository

import com.costostudio.ninao.domain.model.UserInfo
import com.costostudio.ninao.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun saveUser(
        uid: String,
        firstName: String,
        lastName: String,
        email: String
    ): Result<Boolean> {
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

    override suspend fun updateUser(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        genre: Int
    ): Result<Boolean> {
        return try {
            val userUpdate = hashMapOf<String, Any>(
                "id" to uid,
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email,
                "genre" to genre
            )
            firestore.collection("users").document(uid).update(userUpdate).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(): Result<UserInfo> {
        return try {
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                return Result.failure(Exception("Utilisateur non connecté"))
            }

            // Récupérer le document de l'utilisateur depuis Firestore
            val documentSnapshot = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(UserInfo::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Erreur de conversion des données utilisateur"))
                }
            } else {
                Result.failure(Exception("Utilisateur non trouvé dans Firestore"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}