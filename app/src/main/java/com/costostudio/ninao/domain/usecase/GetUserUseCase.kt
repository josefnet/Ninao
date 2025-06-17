package com.costostudio.ninao.domain.usecase

import com.costostudio.ninao.domain.model.UserInfo

interface GetUserUseCase {
    suspend fun execute(): Result<UserInfo>
}