package com.costostudio.ninao.domain.usecase

import com.costostudio.ninao.domain.model.UserInfo
import com.costostudio.ninao.domain.repository.UserRepository

class GetUserUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserUseCase {
    override suspend fun execute(): Result<UserInfo> {
       return userRepository.getUser()
    }
}