package com.kk.android.bayareanews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CompletableDeferred

@HiltAndroidApp
class NewsReaderApp: Application() {

    val remoteConfigResponse = CompletableDeferred<Boolean>()

    companion object {
        lateinit var app: NewsReaderApp
    }
    override fun onCreate() {
        super.onCreate()
        app = this
    }
}