package com.bobryshev.currency.utils

import android.content.res.Resources
import androidx.annotation.StringRes

object StringFormatter {

    private lateinit var resources: Resources

    fun setRes(resources: Resources) {
        this.resources = resources
    }

    fun from(@StringRes id: Int): String =
        resources.getString(id)

    fun from(@StringRes formatter: Int, vararg args: Any): String =
        resources.getString(formatter, *args)

    fun from(@StringRes formatter: Int, vararg args: String): String =
        resources.getString(formatter, *args)


}