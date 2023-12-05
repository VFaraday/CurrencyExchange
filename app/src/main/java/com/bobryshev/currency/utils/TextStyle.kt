package com.bobryshev.currency.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.bobryshev.currency.R

val bitTextStyle = TextStyle(
    fontSize = 20.sp,
    fontFamily = Font(R.font.roboto_mono).toFontFamily(),
    color = Color.Black
)

val textStyle = TextStyle(
    fontSize = 16.sp,
    fontFamily = Font(R.font.roboto_mono).toFontFamily(),
    color = Color.Black
)

val lightTextStyle = TextStyle(
    fontSize = 16.sp,
    fontFamily = Font(R.font.roboto_mono).toFontFamily(),
    color = Color.Black.copy(alpha = 0.5f)
)