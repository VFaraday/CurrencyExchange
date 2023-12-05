package com.bobryshev.currency.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<STATE: UIState>: ViewModel() {

    private var _uiState = MutableStateFlow(initalState)
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, throwable ->

    }

    protected fun updateState(block: (currentState: MutableStateFlow<STATE>) -> Unit) {
        block.invoke(_uiState)
    }

    abstract fun handleUiEvent(event: UiIntent)

    abstract val initalState: STATE

    protected fun launch(
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context = EmptyCoroutineContext + handler, block = block)
}