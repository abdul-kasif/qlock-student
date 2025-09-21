// ui/components/lockdown/LockdownWebView.kt
package com.example.qlockstudentapp.ui.components.lockdown

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LockdownWebView(
    modifier: Modifier = Modifier,
    url: String,
    onWebViewLoaded: () -> Unit = {}
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.userAgentString = "QLockStudentApp/1.0"
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onWebViewLoaded()
                    }
                }
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        },
        modifier = modifier.fillMaxSize()
    )
}