package com.costostudio.ninao.domain.usecase

interface RegisterUseCase {
    suspend fun execute(firstName: String, lastName: String, email: String, password: String): Result<String>
}