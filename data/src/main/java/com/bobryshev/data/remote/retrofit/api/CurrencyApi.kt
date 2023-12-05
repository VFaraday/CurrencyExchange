package com.bobryshev.data.remote.retrofit.api

import com.bobryshev.data.remote.model.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {

    @GET("currency-exchange-rates")
    suspend fun getCurrencyRates(): Response<CurrencyRateResponse>
}