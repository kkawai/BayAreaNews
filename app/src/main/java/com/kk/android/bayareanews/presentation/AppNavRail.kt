package com.kk.android.bayareanews.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.presentation.ui.common.Screen
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme

@Composable
fun AppNavRail(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {
            Icon(
                imageVector = Icons.Filled.Home,
                null,
                Modifier.padding(vertical = 12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = currentRoute == Screen.HomeScreen.route,
            onClick = navigateToHome,
            icon = { Icon(Icons.Filled.Home, stringResource(R.string.home_title)) },
            label = { Text(stringResource(R.string.home_title)) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == Screen.FavoritesScreen.route,
            onClick = navigateToFavorites,
            icon = { Icon(Icons.Filled.Favorite, stringResource(R.string.favorites)) },
            label = { Text(stringResource(R.string.favorites)) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == Screen.PrivacyPolicyScreen.route,
            onClick = navigateToPrivacyPolicy,
            icon = { Icon(Icons.Filled.PrivacyTip, stringResource(R.string.privacy_policy_title)) },
            label = { Text(stringResource(R.string.privacy_policy_title)) },
            alwaysShowLabel = false
        )
        Spacer(Modifier.weight(1f))
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppNavRail() {
    BayAreaNewsTheme {
        AppNavRail(
            currentRoute = Screen.HomeScreen.route,
            navigateToHome = {},
            navigateToFavorites = {},
            navigateToPrivacyPolicy = {}
        )
    }
}
