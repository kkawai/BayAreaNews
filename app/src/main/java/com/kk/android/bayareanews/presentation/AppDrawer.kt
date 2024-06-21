package com.kk.android.bayareanews.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.common.PlaystoreUtil
import com.kk.android.bayareanews.presentation.ui.common.Screen
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToOaklandHome: () -> Unit,
    navigateToSanJoseHome: () -> Unit,
    navigateToNorthBayHome: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToContactInfo: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ModalDrawerSheet(modifier.fillMaxWidth(.8f)) {
        BayAreaNewsLogo(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.home_title)) },
            icon = { Icon(Icons.Filled.Home, null) },
            selected = currentRoute == Screen.HomeScreen.route,
            onClick = { navigateToHome(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.home_oakland_title)) },
            icon = { Icon(Icons.Filled.Map, null) },
            selected = currentRoute == Screen.HomeOaklandScreen.route,
            onClick = { navigateToOaklandHome(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.home_san_jose_title)) },
            icon = { Icon(Icons.Filled.Map, null) },
            selected = currentRoute == Screen.HomeSanJoseScreen.route,
            onClick = { navigateToSanJoseHome(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.home_north_bay_title)) },
            icon = { Icon(Icons.Filled.Map, null) },
            selected = currentRoute == Screen.HomeNorthBayScreen.route,
            onClick = { navigateToNorthBayHome(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.favorites)) },
            icon = { Icon(Icons.Filled.Favorite, null) },
            selected = currentRoute == Screen.FavoritesScreen.route,
            onClick = { navigateToFavorites()
                        closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.contact_info)) },
            icon = { Icon(Icons.Filled.Email, null) },
            selected = currentRoute == Screen.ContactInfoScreen.route,
            onClick = { navigateToContactInfo()
                closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.privacy_policy_title)) },
            icon = { Icon(Icons.Filled.PrivacyTip, null) },
            selected = currentRoute == Screen.PrivacyPolicyScreen.route,
            onClick = { navigateToPrivacyPolicy()
                        closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.playstore_update)) },
            icon = { Icon(Icons.Outlined.PlayArrow, null) },
            selected = false,
            onClick = { PlaystoreUtil.open(context)
                closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
private fun BayAreaNewsLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(text = stringResource(R.string.app_name))
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLogo() {
    BayAreaNewsTheme {
        BayAreaNewsLogo()
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppDrawer() {
    BayAreaNewsTheme {
        AppDrawer(
            currentRoute = Screen.HomeScreen.route,
            navigateToHome = {},
            navigateToOaklandHome = {},
            navigateToSanJoseHome = {},
            navigateToNorthBayHome = {},
            navigateToFavorites = {},
            navigateToContactInfo = {},
            navigateToPrivacyPolicy = {},
            closeDrawer = {}
        )
    }
}