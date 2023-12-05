package com.bobryshev.data.remote.impl

import com.bobryshev.data.datasource.remote.CurrencyDataSource
import com.bobryshev.data.remote.handleApi
import com.bobryshev.data.remote.retrofit.api.CurrencyApi
import com.bobryshev.domain.NetworkResult
import com.bobryshev.domain.model.Rate
import javax.inject.Inject

class CurrencyDataSourceImpl @Inject constructor(
    private val api: CurrencyApi
): CurrencyDataSource {

    override suspend fun getCurrencyRates(): NetworkResult<List<Rate>> {
        return handleApi(
            execute = { api.getCurrencyRates() },
            mapper = { response ->
                response.rates.toList().map {
                    Rate(it.first, it.second)
                }
            }
        )
    }
}