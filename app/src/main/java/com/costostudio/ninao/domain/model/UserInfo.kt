package com.costostudio.ninao.domain.model

data class UserInfo(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val genre: Int = 0,
    val imageUrl: String = ""
)