package com.costostudio.ninao.domain.model

sealed class CustomResult<out T> {
    data class Success<out T>(val data: T) : CustomResult<T>()
    data class Error(val message: String) : CustomResult<Nothing>()
}