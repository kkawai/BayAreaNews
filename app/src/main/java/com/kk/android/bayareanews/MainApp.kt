package com.kk.android.bayareanews

import android.app.Application
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CompletableDeferred

@HiltAndroidApp
class MainApp: Application() {

    val remoteConfigResponse = CompletableDeferred<Boolean>()
    var remoteConfigMap: Map<String, FirebaseRemoteConfigValue> = emptyMap()

    companion object {
        lateinit var app: MainApp
    }
    override fun onCreate() {
        super.onCreate()
        app = this
    }
}