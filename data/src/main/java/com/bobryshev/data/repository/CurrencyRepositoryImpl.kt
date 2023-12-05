package com.bobryshev.data.repository

import com.bobryshev.data.datasource.remote.CurrencyDataSource
import com.bobryshev.domain.NetworkResult
import com.bobryshev.domain.model.Rate
import com.bobryshev.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDataSource: CurrencyDataSource
): CurrencyRepository {

    override suspend fun getCurrencyRates(): NetworkResult<List<Rate>> {
        return currencyDataSource.getCurrencyRates()
    }
}