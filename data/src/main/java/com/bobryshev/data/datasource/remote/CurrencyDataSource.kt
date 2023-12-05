package com.bobryshev.data.datasource.remote

import com.bobryshev.domain.NetworkResult
import com.bobryshev.domain.model.Rate

interface CurrencyDataSource {

    suspend fun getCurrencyRates(): NetworkResult<List<Rate>>
}