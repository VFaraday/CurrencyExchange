package com.bobryshev.currency.view.mainscreen

import com.bobryshev.currency.base.UiIntent

sealed interface CurrencyUiIntent: UiIntent

data object LoadData: CurrencyUiIntent

data class UpdateSell(val value: Float): CurrencyUiIntent

data class UpdateSellRate(val rate: String): CurrencyUiIntent

data class UpdateReceiveRate(val rate: String): CurrencyUiIntent

data class Exchange(val sellRate: String, val sellValue: Float): CurrencyUiIntent