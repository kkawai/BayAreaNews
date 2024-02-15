package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import com.tapresearch.tapsdk.models.TRReward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RewardViewModel: ViewModel() {

    private val rewards: MutableList<TRReward> = ArrayList()

    private val _trInitReady = MutableStateFlow(false)
    val trInitReady = _trInitReady.asStateFlow()

    private val _showRewardScreenStateFlow = MutableStateFlow(false)
    val showRewardsStateFlow = _showRewardScreenStateFlow.asStateFlow()

    fun setReady() {
        _trInitReady.update { true }
    }

    fun hideRewardScreen() {
        _showRewardScreenStateFlow.update { false }
    }

    private fun showRewardScreen() {
        _showRewardScreenStateFlow.update { true }
    }
    fun addRewards(rewards: List<TRReward>) {
        for (r in rewards) {
            if (!this.rewards.contains(r)) {
                this.rewards.add(r)
            }
        }
        showRewardScreen()
    }

    fun getRewards(): List<TRReward> {
        return rewards
    }

}