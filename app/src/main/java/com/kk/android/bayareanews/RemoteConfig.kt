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

class RemoteConfig {

    private var remoteConfigTimeout: Job? = null
    fun fetch(activity: Activity) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity) { task ->
                remoteConfigTimeout?.cancel()
                MLog.i("nnnnn", "MainActivity received remote from firebase")
                MainApp.app.remoteConfigMap = remoteConfig.all
                MainApp.app.remoteConfigResponse.complete(task.isSuccessful)
            }
        remoteConfigTimeout = CoroutineScope(Dispatchers.IO).launch {
            if (!MainApp.app.remoteConfigResponse.isCompleted) {
                MLog.i("nnnnn", "MainActivity to allow 5 seconds to fetch remote config")
                delay(5000) //wait max 4 seconds for config response
                MainApp.app.remoteConfigResponse.complete(false)
                MLog.i("nnnnn", "MainActivity. waited 5 seconds to fetch remote config")
            }
        }
        remoteConfigTimeout?.start()
    }

    fun cancel() {
        remoteConfigTimeout?.cancel()
    }
}