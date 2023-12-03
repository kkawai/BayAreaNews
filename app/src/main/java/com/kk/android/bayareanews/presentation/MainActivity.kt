package com.kk.android.bayareanews.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.kk.android.bayareanews.NewsReaderApp
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme
import dagger.hilt.android.AndroidEntryPoint

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
    }
}
