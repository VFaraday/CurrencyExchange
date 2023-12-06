package com.bobryshev.currency.view.mainscreen

import com.bobryshev.currency.base.UiEffect

sealed interface CurrencyUiEffect: UiEffect

data class ShowToast(val message: String): CurrencyUiEffect

data class ShowDialog(val title: String?, val message: String?): CurrencyUiEffect