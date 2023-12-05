package com.bobryshev.domain

sealed interface NetworkResult<T : Any?>
class Success<T: Any>(val data: T) : NetworkResult<T>
class Error<T: Any>(val code: Int, val message: String?) : NetworkResult<T>
class Exception<T: Any>(val e: Throwable) : NetworkResult<T>

suspend fun <T : Any> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> NetworkResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): NetworkResult<T> = apply {
    if (this is Error<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> NetworkResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResult<T> = apply {
    if (this is Exception<T>) {
        executable(e)
    }
}
fun <T: Any, R: Any> NetworkResult<T>.map(
    block: T.() -> R
): NetworkResult<R> = run {
    when(this) {
        is Success -> {
            Success(
                data.block()
            )
        }
        is Error -> {
            Error(this.code, this.message)
        }
        is Exception -> {
            Exception(e)
        }
    }
}