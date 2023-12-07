package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.NewsReaderApp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.Constants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
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
                        Text(text = stringResource(id = R.string.privacy_policy_title))
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
        ) { values ->
            PrivacyPolicyText(text = NewsReaderApp.app.remoteConfigMap
                .get(Constants.PRIVACY_POLICY_V2)?.asString()?:Constants.PRIVACY_POLICY_DEFAULT,
                values)
        }
    }
}

@Composable
fun PrivacyPolicyText(text: String, contentPadding: PaddingValues = PaddingValues(0.dp)) {
    Text(
        modifier = Modifier.padding(top = 70.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        text = text)
}