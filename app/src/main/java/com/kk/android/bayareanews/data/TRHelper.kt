package com.kk.android.bayareanews.data

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.kk.android.bayareanews.R
import com.tapresearch.tapsdk.TapInitOptions
import com.tapresearch.tapsdk.TapResearch
import com.tapresearch.tapsdk.callback.TRContentCallback
import com.tapresearch.tapsdk.callback.TRErrorCallback
import com.tapresearch.tapsdk.callback.TRQQDataCallback
import com.tapresearch.tapsdk.callback.TRRewardCallback
import com.tapresearch.tapsdk.callback.TRSdkReadyCallback
import com.tapresearch.tapsdk.models.QuickQuestion
import com.tapresearch.tapsdk.models.TRError
import com.tapresearch.tapsdk.models.TRReward

interface TRApi {
    fun trInit(userId: String, onTRReady: ()->Unit, onRewards: (List<TRReward>)->Unit)
    fun checkPlacementAvailable(placement: String, onPlacementAvailable: () -> Unit)
    fun showPlacement(placement: String)
}

class TRHelper : TRApi {

    lateinit var context: Context //must be activity context

    private val TAG = "TRHelper"

    override fun trInit(userId: String, onTRReady: ()->Unit, onRewards: (List<TRReward>)->Unit) {
        TapResearch.initialize(
            apiToken = context.getString(R.string.tap_api_token),
            userIdentifier = userId,
            activity = context as Activity,
            rewardCallback =
            object : TRRewardCallback {
                override fun onTapResearchDidReceiveRewards(rewards: MutableList<TRReward>) {
                    onRewards(rewards)
                }
            },
            errorCallback =
            object : TRErrorCallback {
                override fun onTapResearchDidError(trError: TRError) {
                    Toast.makeText(context, "TapError: $trError", Toast.LENGTH_SHORT).show()
                }
            },
            sdkReadyCallback =
            object : TRSdkReadyCallback {
                override fun onTapResearchSdkReady() {
                    Log.d(TAG, "TR SDK ready")
                    // now that the SDK is ready, we can show content
                    onTRReady()
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

    override fun checkPlacementAvailable(placement: String, onPlacementAvailable: () -> Unit) {
        if (TapResearch.canShowContentForPlacement(
                placement,
                errorCallback = object : TRErrorCallback {
                    override fun onTapResearchDidError(trError: TRError) {
                        trError.description?.let {
                            Log.e("TRERROR", it)
                            //Toast.makeText(context, "$placement not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
            )
        ) {
            onPlacementAvailable()
        }
    }

    override fun showPlacement(placement: String) {
        val application = context.applicationContext as Application
        TapResearch.showContentForPlacement(placement, application,
            contentCallback = object : TRContentCallback {
                override fun onTapResearchContentShown(placement: String) {
                    //tapResearchDidDismiss(placement)
                }

                override fun onTapResearchContentDismissed(placement: String) {
                    //tapResearchContentShown(placement)
                }
            },
            errorCallback = object : TRErrorCallback {
                override fun onTapResearchDidError(trError: TRError) {
                    //showErrorToast(trError)
                }
            }
        )
    }
}