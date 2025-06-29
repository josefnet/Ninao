package com.costostudio.ninao.domain.usecase

interface UpdateUserToFireStoreUseCase {
    suspend fun execute(uid: String, firstName: String, lastName: String, email: String, genre: Int, imageUrl: String?): Result<Boolean>
}