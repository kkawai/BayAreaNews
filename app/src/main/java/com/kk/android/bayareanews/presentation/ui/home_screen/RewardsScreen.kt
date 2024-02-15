package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.R
import com.tapresearch.tapsdk.models.TRReward
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    rewardsFlow: StateFlow<RewardsState>,
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
            val rewards = rewardsFlow.collectAsState()
            val set = HashSet<String>()
            //PrivacyPolicyText(text = MainApp.app.remoteConfigMap
            //    .get(Constants.PRIVACY_POLICY_V2)?.asString()?:Constants.PRIVACY_POLICY_DEFAULT,
            //    screenModifier)
            Column() {
                if (rewards.value.rewardList.isNotEmpty()) {
                    for (reward in rewards.value.rewardList) {
                        if (!set.contains(reward.transactionIdentifier)) {
                            Row() {
                                DisplayRewardItem(reward = reward)
                            }
                        } else {
                            reward.transactionIdentifier?.let { set.add(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayRewardItem(reward: TRReward) {
    reward.currencyName?.let { Text(modifier = Modifier.padding(8.dp), text = it) }
    reward.rewardAmount?.let { Text(modifier = Modifier.padding(8.dp), text = ""+it) }
    reward.placementTag?.let { Text(modifier = Modifier.padding(8.dp), text = it) }
}
