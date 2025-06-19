package com.costostudio.ninao.domain.util

sealed class CustomResource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : CustomResource<T>(data)
    class Error<T>(message: String, data: T? = null) : CustomResource<T>(data, message)
    class Loading<T>(data: T? = null) : CustomResource<T>(data)
}