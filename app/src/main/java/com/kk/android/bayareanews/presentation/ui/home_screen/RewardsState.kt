package com.kk.android.bayareanews.presentation.ui.home_screen

import com.tapresearch.tapsdk.models.TRReward

data class RewardsState(
    val rewardList: List<TRReward> = emptyList(),
)