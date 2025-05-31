package com.costostudio.ninao.domain.usecase

import com.costostudio.ninao.domain.repository.UserRepository

class SaveUserToFireStoreUseCaseImpl(
    private val userRepository: UserRepository
): SaveUserToFireStoreUseCase {
    override suspend fun execute(
        uid: String,
        firstName: String,
        lastName: String,
        email: String
    ): Result<Boolean> {
        return userRepository.saveUser(uid, firstName, lastName, email)
    }
}