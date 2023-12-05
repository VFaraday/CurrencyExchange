package com.bobryshev.currency.view.mainscreen

import com.bobryshev.currency.base.BaseViewModel
import com.bobryshev.currency.base.UiIntent
import com.bobryshev.currency.utils.Util
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.onError
import com.bobryshev.domain.onSuccess
import com.bobryshev.domain.usecase.CurrencyRateUseCase
import com.bobryshev.domain.usecase.GetBalanceUseCase
import com.bobryshev.domain.usecase.UpdateBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyUseCase: CurrencyRateUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val updateBalanceUseCase: UpdateBalanceUseCase
): BaseViewModel<CurrencyUiState, CurrencyUiEffect>() {

    override val initalState: CurrencyUiState
        get() = CurrencyUiState()

    override fun handleUiEvent(event: UiIntent) {
        when(event) {
            LoadData -> {
                loadUserBalance()
                loadRates()
            }
            is Exchange -> updateUserBalance()
            is UpdateSell -> calculateReceive(event.value)
            is UpdateReceiveRate -> updateReceiveRate(event.rate)
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

    private fun updateUserBalance() {
        launch {
            val receiveRate = uiState.value.receiveRate
            val sellValue = uiState.value.sellValue
            val sellRate = uiState.value.sellRate
            var currentBalanceValue = 0.00

            uiState.value.userBalance.first()

            if (receiveRate.isEmpty() || sellRate.isEmpty() || sellValue == 0.00) {
                setEffect {
                    ShowToast("Please select all data")
                }
                return@launch
            }

            if (currentBalanceValue - sellValue < 0.00) {
                setEffect {
                    ShowToast("Your balance is not enough")
                }
                return@launch
            }
            updateBalanceUseCase.invoke(Balance(uiState.value.receiveRate, uiState.value.receiveValue))
        }
    }

    private fun updateReceiveRate(rate: String) {
        updateState { state->
            state.value = state.value.copy(
                receiveRate = rate
            )
        }
        calculateReceive(uiState.value.sellValue)
    }

    private fun calculateReceive(value: Double) {
        updateState { state ->
            val receiveRate = state.value.receiveRate
            if (receiveRate.isEmpty()) {
                state.value = state.value.copy(
                    sellValue = value,
                )
                return@updateState
            }

            state.value.rates.find { rate -> rate.rateName == receiveRate }?.let {
                state.value = state.value.copy(
                    sellValue = value,
                    receiveValue = Util.roundOffDecimal(value * it.value)
                )
            }
        }
    }
}