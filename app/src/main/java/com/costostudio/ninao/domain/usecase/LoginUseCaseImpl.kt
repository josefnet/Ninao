package com.costostudio.ninao.domain.usecase

import com.costostudio.ninao.domain.repository.AuthRepository

class LoginUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginUseCase {
    override suspend fun execute(email: String, password: String): Result<String> {
        return authRepository.login(email, password)
    }
}