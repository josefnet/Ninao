package com.costostudio.ninao.domain.repository

import com.costostudio.ninao.domain.model.UserInfo


interface UserRepository {
    suspend fun saveUser(uid: String, firstName: String, lastName: String, email: String): Result<Boolean>
    suspend fun updateUser(uid: String, firstName: String, lastName: String, email: String, genre: Int, imageUrl: String?): Result<Boolean>
    suspend fun getUser(): Result<UserInfo>
}