package com.costostudio.ninao.domain.usecase

import com.costostudio.ninao.domain.repository.UserRepository

class UpdateUserToFireStoreUseCaseImpl(
    private val userRepository: UserRepository
): UpdateUserToFireStoreUseCase {
    override suspend fun execute(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        genre: Int,
        imageUrl: String?
    ): Result<Boolean> {
        return userRepository.updateUser(uid, firstName, lastName, email, genre, imageUrl)
    }
}