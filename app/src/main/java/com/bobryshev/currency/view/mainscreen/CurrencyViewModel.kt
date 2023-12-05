package com.bobryshev.currency.view.mainscreen

import com.bobryshev.currency.base.BaseViewModel
import com.bobryshev.currency.base.UiIntent
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.onError
import com.bobryshev.domain.onSuccess
import com.bobryshev.domain.usecase.CurrencyRateUseCase
import com.bobryshev.domain.usecase.GetBalanceUseCase
import com.bobryshev.domain.usecase.UpdateBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyUseCase: CurrencyRateUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val updateBalanceUseCase: UpdateBalanceUseCase
): BaseViewModel<CurrencyUiState>() {

    override val initalState: CurrencyUiState
        get() = CurrencyUiState()

    override fun handleUiEvent(event: UiIntent) {
        when(event) {
            LoadData -> {
                loadUserBalance()
                loadRates()
            }
            is Exchange -> updateUserBalance(Balance(event.sellRate, event.sellValue))
        }
    }


    private fun loadRates() {
        launch {
            currencyUseCase.invoke()
                .onSuccess { rates ->
                    updateState { state ->
                        state.value = state.value.copy(
                            rates = rates
                        )
                    }
                }
                .onError { code, message ->

                }
        }
    }

    private fun loadUserBalance() {
        launch {
            getBalanceUseCase.invoke().apply {
                updateState { state ->
                    state.value = state.value.copy(
                        userBalance = this
                    )
                }
            }
        }
    }

    private fun updateUserBalance(balance: Balance) {
        launch {
            updateBalanceUseCase.invoke(balance)
        }
    }

    private fun calculateReceive() {

    }
}