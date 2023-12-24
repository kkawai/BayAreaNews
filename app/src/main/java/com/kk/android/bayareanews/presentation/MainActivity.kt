package com.kk.android.bayareanews.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.kk.android.bayareanews.RemoteConfig
import com.kk.android.bayareanews.common.speech.GetSpeech
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.ref.WeakReference


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val speechFlow = MutableStateFlow("")

    private val getSpeech = registerForActivityResult(GetSpeech()) { speechText: String ->
        speechFlow.update {
            speechText
        }
        Log.i("ggggg", "ActivityResultCallback speechText $speechText")
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            BayAreaNewsApp(widthSizeClass, speechFlow, {getSpeech.launch(Unit)})
        }
        RemoteConfig(lifecycleScope, WeakReference<Activity>(this)).fetch()
    }
}
