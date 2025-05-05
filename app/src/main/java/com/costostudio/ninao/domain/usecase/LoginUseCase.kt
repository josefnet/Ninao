package com.costostudio.ninao.domain.usecase


interface LoginUseCase {
    suspend fun execute(email: String, password: String): Result<String>
}