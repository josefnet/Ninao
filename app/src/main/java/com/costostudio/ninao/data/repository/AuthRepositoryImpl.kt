package com.costostudio.ninao.data.repository

import android.util.Log
import com.costostudio.ninao.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid
                ?: return Result.failure(Exception("User ID is null"))
            Result.success(userId)
        } catch (e: FirebaseAuthException) {
            val message = when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> "User not found"
                "ERROR_WRONG_PASSWORD" -> "Incorrect password"
                else -> e.localizedMessage ?: "Login failed"
            }
            Log.e("AuthRepositoryImpl", "Login failed: $message")
            Result.failure(Exception(message))
        } catch (e: Exception) {
            val message = e.localizedMessage ?: "Unknown error"
            Log.e("AuthRepositoryImpl", "Login failed: $message")
            Result.failure(Exception(message))
        }
    }

    override suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID null"))
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Unknown error"))
        }
    }

    override fun logout() {
        auth.signOut()
    }
}