package com.kk.android.bayareanews.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kk.android.bayareanews.NewsReaderRemoteConfig
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val remoteConfig = NewsReaderRemoteConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BayAreaNewsTheme {
                NewsNavHost()
            }
        }
        remoteConfig.fetch(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteConfig.cancel()
    }
}
