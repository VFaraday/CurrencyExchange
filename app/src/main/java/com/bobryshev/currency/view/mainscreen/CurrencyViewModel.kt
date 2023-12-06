package com.bobryshev.currency.view.mainscreen

import com.bobryshev.currency.R
import com.bobryshev.currency.base.BaseViewModel
import com.bobryshev.currency.base.DialogData
import com.bobryshev.currency.base.UiIntent
import com.bobryshev.currency.utils.Constants
import com.bobryshev.currency.utils.StringFormatter
import com.bobryshev.currency.utils.Util
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.model.User
import com.bobryshev.domain.onError
import com.bobryshev.domain.onSuccess
import com.bobryshev.domain.usecase.CurrencyRateUseCase
import com.bobryshev.domain.usecase.GetBalanceUseCase
import com.bobryshev.domain.usecase.GetUserUseCase
import com.bobryshev.domain.usecase.UpdateBalanceUseCase
import com.bobryshev.domain.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyUseCase: CurrencyRateUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val updateBalanceUseCase: UpdateBalanceUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
): BaseViewModel<CurrencyUiState, CurrencyUiEffect>() {

    private var user: User? = null

    override val initalState: CurrencyUiState
        get() = CurrencyUiState()

    override fun handleUiEvent(event: UiIntent) {
        when(event) {
            LoadData -> {
                getUser()
                loadUserBalance()
                loadRates()
            }
            is Exchange -> updateUserBalance()
            is UpdateSell -> calculateReceive(event.value)
            is UpdateReceiveRate -> updateReceiveRate(event.rate)
            is UpdateSellRate -> updateSellRate(event.rate)
        }
    }

    private fun getUser() {
        launch {
            user = getUserUseCase.invoke()
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

            if (receiveRate.isEmpty() || sellRate.isEmpty() || sellValue == 0.00) {
                setEffect {
                    ShowToast(StringFormatter.from(R.string.please_select_all_data))
                }
                return@launch
            }

            val currentBalance = uiState.value.userBalance.first().firstOrNull {
                it.rate == sellRate
            } ?: return@launch

            val receiveBalance: Balance? = uiState.value.userBalance.first().firstOrNull {
                it.rate == receiveRate
            }

            val currentBalanceValue = currentBalance.value

            val newBalanceValue = if ((user?.countOfExchanges ?: 0) > 5) {
                (currentBalanceValue - sellValue) - (sellValue * Constants.COMMISSION_FEE)
            } else {
                currentBalanceValue - sellValue
            }

            if (newBalanceValue < 0.00) {
                setEffect {
                    ShowToast(StringFormatter.from(R.string.your_balance_is_not_enough))
                }
                return@launch
            }

            val newBalance = receiveBalance?.copy(
                value = receiveBalance.value + uiState.value.receiveValue
            ) ?: Balance(Random.nextInt(), uiState.value.receiveRate, uiState.value.receiveValue)
            updateBalanceUseCase.invoke(currentBalance.copy(value = newBalanceValue), newBalance)
            user?.let {
                updateUserUseCase.invoke(it.copy(countOfExchanges = it.countOfExchanges + 1))
            }

            setEffect {
                ShowDialog(
                    dialogData = DialogData(
                        title = "Currency converted",
                        text = "You have converted",
                        onDismissRequest = {
                            updateState { state ->
                                state.value = state.value.copy(
                                    openAlertDialog = false
                                )
                            }
                        },
                        onConfirmation = {
                            updateState { state ->
                                state.value = state.value.copy(
                                    openAlertDialog = false
                                )
                            }
                        },
                    )
                )
            }
            updateState { state ->
                state.value = state.value.copy(
                    openAlertDialog = true
                )
            }
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

    private fun updateSellRate(rate: String) {
        updateState {  state ->
            state.value = state.value.copy(
                sellRate = rate
            )
        }
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