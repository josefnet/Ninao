package com.costostudio.ninao.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> CoroutineScope.execute(
    function: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
    onError: suspend (Throwable) -> Unit
) {
    this.launch {
        val result = withContext(Dispatchers.IO) {
            runCatching { function() }
        }

        result.onSuccess { onSuccess(it) }
            .onFailure { onError(it) }
    }
}