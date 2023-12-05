package com.bobryshev.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AbsUseCase {

    protected suspend inline fun <T> runOnBackground(crossinline block: suspend () -> T) =
        withContext(Dispatchers.IO) {
            block()
        }
}