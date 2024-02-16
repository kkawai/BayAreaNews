package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.R
import com.tapresearch.tapsdk.models.TRReward


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    rewards: List<TRReward>,
    onRewardsShown: ()->Unit,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    onGoBackClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(text = "My Rewards")
                    },
                    navigationIcon = {
                        if (!isExpandedScreen) {
                            IconButton(onClick = openDrawer) {
                                Icon(
                                    imageVector = Icons.Outlined.Menu,
                                    contentDescription = stringResource(
                                        R.string.cd_open_navigation_drawer
                                    ),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    actions = {
                        IconButton(onClick = {
                            onGoBackClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)
            Column(screenModifier) {
                Spacer(Modifier.padding(16.dp))
                if (rewards.isNotEmpty()) {
                    for (reward in rewards) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            DisplayRewardItem(reward = reward)
                        }
                    }
                }
            }
            onRewardsShown()
        }
    }
}

@Composable
private fun DisplayRewardItem(reward: TRReward) {
    Icon(
        imageVector = Icons.Filled.CurrencyBitcoin,
        null,
        Modifier.padding(vertical = 8.dp),
        tint = MaterialTheme.colorScheme.primary
    )
    reward.currencyName?.let { Text(modifier = Modifier.padding(8.dp), text = it) }
    reward.rewardAmount?.let { Text(modifier = Modifier.padding(8.dp), text = ""+it) }
    reward.placementTag?.let { Text(modifier = Modifier.padding(8.dp), text = it) }
}
