package com.bobryshev.data.remote

import com.bobryshev.domain.Error
import com.bobryshev.domain.NetworkResult
import com.bobryshev.domain.Success
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any, R: Any> handleApi(
    execute: suspend () -> Response<T>,
    mapper: (T) -> R
): NetworkResult<R> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Success(mapper(body))
        } else {
            Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        com.bobryshev.domain.Exception(e)
    }
}