package com.bobryshev.currency.view.mainscreen

import com.bobryshev.currency.base.DialogData
import com.bobryshev.currency.base.UiEffect

sealed interface CurrencyUiEffect: UiEffect

data class ShowToast(val message: String): CurrencyUiEffect

data class ShowDialog(val dialogData: DialogData): CurrencyUiEffect