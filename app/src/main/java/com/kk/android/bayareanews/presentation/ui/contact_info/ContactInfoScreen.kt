package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.MainApp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.Constants
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInfoScreen(
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
                        Text(text = stringResource(id = R.string.contact_info))
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
            ContactInfoText(text = MainApp.app.remoteConfigMap
                .get(Constants.CONTACT_INFO)?.asString()?:Constants.CONTACT_INFO_DEFAULT,
                screenModifier)
        }
    }
}

@Composable
fun ContactInfoText(text: String, modifier: Modifier) {
    Text(
        modifier = modifier
            .padding(top = 32.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        text = text)
}

@Preview
@Composable
fun ContactInfoTextPreview() {
    BayAreaNewsTheme {
        Surface {
            Column {
                ContactInfoText(text = Constants.CONTACT_INFO_DEFAULT, modifier = Modifier)
            }
        }
    }
}