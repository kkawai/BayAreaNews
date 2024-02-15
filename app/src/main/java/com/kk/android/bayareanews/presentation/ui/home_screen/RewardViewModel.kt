package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import com.tapresearch.tapsdk.models.TRReward
import kotlinx.coroutines.flow.MutableStateFlow

class RewardViewModel: ViewModel() {

    val rewards: MutableList<TRReward> = ArrayList()
    val showRewardScreen = MutableStateFlow(false)
    fun addRewards(rewards: List<TRReward>) {
        for (r in rewards) {
            if (!this.rewards.contains(r)) {
                this.rewards.add(r)
            }
        }
    }

}