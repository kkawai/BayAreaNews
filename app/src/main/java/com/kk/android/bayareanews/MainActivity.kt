package com.kk.android.bayareanews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kk.android.bayareanews.presentation.NewsNavHost
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
    }
}
