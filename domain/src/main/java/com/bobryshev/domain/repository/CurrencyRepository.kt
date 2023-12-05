package com.bobryshev.domain.repository

import com.bobryshev.domain.NetworkResult
import com.bobryshev.domain.model.Rate

interface CurrencyRepository {

    suspend fun getCurrencyRates(): NetworkResult<List<Rate>>
}