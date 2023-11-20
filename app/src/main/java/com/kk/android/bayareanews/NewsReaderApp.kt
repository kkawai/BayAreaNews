package com.kk.android.bayareanews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsReaderApp: Application() {

    companion object {
        lateinit var app: NewsReaderApp
    }
    override fun onCreate() {
        super.onCreate()
        app = this
    }
}