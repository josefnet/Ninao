package com.costostudio.ninao.domain.usecase

import com.costostudio.ninao.domain.repository.UserRepository

class UpdateUserToFireStoreUseCaseImpl(
    private val userRepository: UserRepository
): UpdateUserToFireStoreUseCase {
    override suspend fun execute(
        uid: String,
        firstName: String,
        lastName: String,
        email: String
    ): Result<Boolean> {
        return userRepository.updateUser(uid, firstName, lastName, email)
    }
}