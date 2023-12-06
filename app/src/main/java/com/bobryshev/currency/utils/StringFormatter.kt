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



}