package com.kk.android.bayareanews.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.kk.android.bayareanews.NewsReaderApp
import com.kk.android.bayareanews.common.MLog
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BayAreaNewsTheme {
                NewsNavHost()
            }
        }
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                NewsReaderApp.app.remoteConfigResponse.complete(task.isSuccessful)
            }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (!NewsReaderApp.app.remoteConfigResponse.isCompleted) {
                    MLog.i("nnnnn","MainActivity about to delay 3 seconds")
                    delay(3000)
                    NewsReaderApp.app.remoteConfigResponse.complete(false)
                    MLog.i("nnnnn","MainActivity finished delay of 3 seconds")
                }
            }catch (ignored: Throwable){}
        }.start()
    }
}
