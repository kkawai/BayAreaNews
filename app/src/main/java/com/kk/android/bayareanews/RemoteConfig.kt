package com.kk.android.bayareanews

import android.app.Activity
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.kk.android.bayareanews.common.MLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class RemoteConfig(
    private val lifecycleCoroutineScope: LifecycleCoroutineScope,
    private val activity: WeakReference<Activity>
) {

    private var job: Job? = null

    fun fetch() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        activity.get()?.let {activity ->
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(activity) { task ->
                    job?.cancel()
                    MLog.i("nnnnn", "MainActivity received remote config from firebase: ${task.isSuccessful}")
                    MainApp.app.remoteConfigMap = remoteConfig.all
                    MainApp.app.remoteConfigResponse.complete(task.isSuccessful)
                }
        }
        job = lifecycleCoroutineScope.launch {
            if (!MainApp.app.remoteConfigResponse.isCompleted) {
                MLog.i("nnnnn", "MainActivity 11 seconds max to fetch remote config")
                delay(11000) //wait max 4 seconds for config response
                MainApp.app.remoteConfigResponse.complete(false)
                MLog.i("nnnnn", "MainActivity. waited 11 seconds. remote config did not respond")
            }
        }
        job?.start()
    }

}