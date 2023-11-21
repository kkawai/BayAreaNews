package com.kk.android.bayareanews.presentation.ui

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kk.android.bayareanews.common.Util

@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current
    AndroidView(factory = {
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
            loadUrl(if (url.startsWith("http")) url else Util.decodeUrlSafe(url))
        }
    })
}

fun loadErrorPage(webview: WebView) {
    val htmlData =
        "<html><body><br/><br/><br/><br/><div align=\"center\" >No internet connection.  Go back and try again.</div></body>"
    webview.loadUrl("about:blank")
    webview.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    webview.invalidate()
}
