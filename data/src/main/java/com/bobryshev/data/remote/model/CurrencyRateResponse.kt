package com.bobryshev.data.remote.model

data class CurrencyRateResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Float>
)