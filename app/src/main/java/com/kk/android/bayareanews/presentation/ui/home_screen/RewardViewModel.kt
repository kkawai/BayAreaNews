package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import com.kk.android.bayareanews.data.TRApi
import com.tapresearch.tapsdk.models.TRReward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RewardViewModel(private val trApi: TRApi) : ViewModel() {

    private val myUserIdentifier = "theinterviewer-1" //2,3

    private val rewards: MutableList<TRReward> = ArrayList()

    private val _showRewardScreenStateFlow = MutableStateFlow(false)
    val showRewardScreenStateFlow = _showRewardScreenStateFlow.asStateFlow()

    private val _isHomePlacementAvailable = MutableStateFlow(false)
    val isHomePlacementAvailable = _isHomePlacementAvailable.asStateFlow()

    private val _isEarnCenterPlacementAvailable = MutableStateFlow(false)
    val isEarnCenterPlacementAvailable = _isEarnCenterPlacementAvailable.asStateFlow()

    private val _isSurveyWallPlacementAvailable = MutableStateFlow(false)
    val isSurveyWallPlacementAvailable = _isSurveyWallPlacementAvailable.asStateFlow()

    private fun onTRReady() {
        trApi.checkPlacementAvailable(
            "home-screen",
            { _isHomePlacementAvailable.update { true } })
        trApi.checkPlacementAvailable(
            "earn-center",
            { _isEarnCenterPlacementAvailable.update { true } })
        trApi.checkPlacementAvailable(
            "survey-wall",
            { _isSurveyWallPlacementAvailable.update { true } })
    }

    init {
        trApi.trInit(myUserIdentifier, { onTRReady() }, { rewards -> addRewards(rewards) })
    }

    fun showHomeScreenPlacement() {
        trApi.showPlacement("home-screen")
    }

    fun showEarnCenterPlacement() {
        trApi.showPlacement("earn-center")
    }

    fun showSurveyWallPlacement() {
        trApi.showPlacement("survey-wall")
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