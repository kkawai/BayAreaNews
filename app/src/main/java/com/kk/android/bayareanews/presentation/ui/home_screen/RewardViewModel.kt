package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import com.tapresearch.tapsdk.models.TRReward

class RewardViewModel: ViewModel() {

    var rewards: List<TRReward> = emptyList()
}