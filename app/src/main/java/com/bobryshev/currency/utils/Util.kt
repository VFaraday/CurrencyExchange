package com.bobryshev.currency.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object Util {

    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }
}