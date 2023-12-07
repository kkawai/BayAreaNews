package com.kk.android.bayareanews

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.kk.android.bayareanews.common.MLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsReaderRemoteConfig {

    private var remoteConfigTimeout: Job? = null
    fun fetch(activity: Activity) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity) { task ->
                remoteConfigTimeout?.cancel()
                MLog.i("nnnnn", "MainActivity received remote from firebase")
                NewsReaderApp.app.remoteConfigMap = remoteConfig.all
                NewsReaderApp.app.remoteConfigResponse.complete(task.isSuccessful)
            }
        remoteConfigTimeout = CoroutineScope(Dispatchers.IO).launch {
            if (!NewsReaderApp.app.remoteConfigResponse.isCompleted) {
                MLog.i("nnnnn", "MainActivity about to delay 5 seconds")
                delay(5000) //wait max 4 seconds for config response
                NewsReaderApp.app.remoteConfigResponse.complete(false)
                MLog.i("nnnnn", "MainActivity finished delay of 5 seconds")
            }
        }
        remoteConfigTimeout?.start()
    }

    fun cancel() {
        remoteConfigTimeout?.cancel()
    }
}