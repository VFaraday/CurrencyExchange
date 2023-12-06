package com.bobryshev.currency

import android.app.Application
import com.bobryshev.currency.utils.StringFormatter
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        StringFormatter.setRes(resources)
    }
}