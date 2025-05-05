package com.costostudio.ninao.domain.repository

import com.costostudio.ninao.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(email: String, password: String): Result<String>
    fun getCurrentUser(): User?
    fun logout()
}