package com.costostudio.ninao.domain.usecase

interface SaveUserToFireStoreUseCase {
    suspend fun execute(uid: String, firstName: String, lastName: String, email: String): Result<Boolean>
}