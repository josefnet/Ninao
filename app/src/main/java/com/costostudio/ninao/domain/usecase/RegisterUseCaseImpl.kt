package com.costostudio.ninao.domain.usecase

import com.costostudio.ninao.domain.repository.AuthRepository

class RegisterUseCaseImpl(
    private val authRepository: AuthRepository,
): RegisterUseCase {
    override suspend fun execute(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<String> {
        return authRepository.register(email, password)
    }
}