package com.costostudio.ninao.domain.repository


interface UserRepository {
    suspend fun saveUser(uid: String, firstName: String, lastName: String, email: String): Result<Boolean>
}