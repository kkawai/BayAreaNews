package com.kk.android.bayareanews.presentation.ui.common

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kk.android.bayareanews.common.EncodingUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    url: String,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    onGoBackClicked: () -> Unit) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            /*
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
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
            } //topBar
            */

        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)
            val context = LocalContext.current
            AndroidView(
                modifier = screenModifier,
                factory = {
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onReceivedError(
                            view: WebView,
                            request: WebResourceRequest,
                            error: WebResourceError
                        ) {
                            loadErrorPage(view)
                        }
                    }
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false
                    loadUrl(if (url.startsWith("http")) url else EncodingUtil.decodeUrlSafe(url))
                }
            })
        }
    }
}

fun loadErrorPage(webview: WebView) {
    val htmlData =
        "<html><body><br/><br/><br/><br/><div align=\"center\" >No internet connection.  Go back and try again.</div></body>"
    webview.loadUrl("about:blank")
    webview.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    webview.invalidate()
}
