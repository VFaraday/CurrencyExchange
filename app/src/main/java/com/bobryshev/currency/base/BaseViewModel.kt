package com.bobryshev.currency.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<State: UiState, Effect: UiEffect>: ViewModel() {

    private var _uiState = MutableStateFlow(initalState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _effect : Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, throwable ->

    }

    protected fun updateState(block: (currentState: MutableStateFlow<State>) -> Unit) {
        block.invoke(_uiState)
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    abstract fun handleUiEvent(event: UiIntent)

    abstract val initalState: State

    protected fun launch(
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context = EmptyCoroutineContext + handler, block = block)
}