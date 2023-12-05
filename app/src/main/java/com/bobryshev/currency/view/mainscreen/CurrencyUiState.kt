package com.bobryshev.currency.view.mainscreen

import com.bobryshev.currency.base.UIState
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.model.Rate
import com.bobryshev.domain.model.UserBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CurrencyUiState(
    val rates: List<Rate> = emptyList(),
    val userBalance: Flow<List<Balance>> = emptyFlow(),
    val receiveValue: Double = 0.00,
    val sellValue: Double = 0.00,
    val sellRate: String = "",
    val receiveRate: String = ""
): UIState