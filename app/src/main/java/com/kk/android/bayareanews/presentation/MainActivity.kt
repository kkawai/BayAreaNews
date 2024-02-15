package com.kk.android.bayareanews.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.RemoteConfig
import com.kk.android.bayareanews.common.speech.GetSpeech
import com.tapresearch.tapsdk.TapInitOptions
import com.tapresearch.tapsdk.TapResearch
import com.tapresearch.tapsdk.callback.TRErrorCallback
import com.tapresearch.tapsdk.callback.TRQQDataCallback
import com.tapresearch.tapsdk.callback.TRRewardCallback
import com.tapresearch.tapsdk.callback.TRSdkReadyCallback
import com.tapresearch.tapsdk.models.QuickQuestion
import com.tapresearch.tapsdk.models.TRError
import com.tapresearch.tapsdk.models.TRReward
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.ref.WeakReference


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = "MainActvty" 
    private val myUserIdentifier = "theinterviewer-1"
    //private val myUserIdentifier2 = "theinterviewer-2"
    //private val myUserIdentifier3 = "theinterviewer-3"
    private val tapInitState = MutableStateFlow(false)

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
            BayAreaNewsApp(tapInitState, widthSizeClass, speechFlow, { getSpeech.launch(Unit) })
        }
        RemoteConfig(lifecycleScope, WeakReference<Activity>(this)).fetch()
        initTap()
    }

    private fun initTap() {
        Log.d(TAG, "API Token: ${getString(R.string.tap_api_token)}")
        Log.d(TAG, "User identifier: $myUserIdentifier")
        TapResearch.initialize(
            apiToken = getString(R.string.tap_api_token),
            userIdentifier = myUserIdentifier,
            activity = this@MainActivity,
            rewardCallback =
            object : TRRewardCallback {
                override fun onTapResearchDidReceiveRewards(rewards: MutableList<TRReward>) {
                    //showRewardToast(rewards)
                    Toast.makeText(this@MainActivity, "Rewarded: $rewards", Toast.LENGTH_SHORT).show()
                    Log.i(TAG,"Rewarded: $rewards")
                }
            },
            errorCallback =
            object : TRErrorCallback {
                override fun onTapResearchDidError(trError: TRError) {
                    //showErrorToast(trError)
                    Toast.makeText(this@MainActivity, "TapError: $trError", Toast.LENGTH_SHORT).show()
                }
            },
            sdkReadyCallback =
            object : TRSdkReadyCallback {
                override fun onTapResearchSdkReady() {
                    Log.d(TAG, "SDK is ready")
                    // now that the SDK is ready, we can show content
                    Toast.makeText(this@MainActivity, "Tap Ready", Toast.LENGTH_SHORT).show()
                    tapInitState.update { true }
                }
            },
            tapDataCallback = object: TRQQDataCallback {
                override fun onQuickQuestionDataReceived(data: QuickQuestion) {

                }

            },
            initOptions =
            TapInitOptions(
                userAttributes =
                hashMapOf(
                    "is_vip" to 1,
                    "something_else" to 2,
                ),
                clearPreviousAttributes = true,
            ),
        )
    }
}
