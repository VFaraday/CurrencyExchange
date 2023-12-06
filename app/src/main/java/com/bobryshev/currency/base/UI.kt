package com.bobryshev.currency.base

import androidx.annotation.StringRes
import com.bobryshev.currency.R

interface UiIntent

interface UiState

interface UiEffect

data class DialogData(
    val title: String?,
    val text: String?,
    val onDismissRequest: () -> Unit,
    val onConfirmation: () -> Unit,
    @StringRes val positiveBtnRes: Int = R.string.button_ok,
    @StringRes val negativeBtnRes: Int? = R.string.button_cancel,
)