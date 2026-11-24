package com.stormchart.app

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private lateinit var assetLoader: WebViewAssetLoader

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window.statusBarColor = Color.rgb(8, 13, 22)
        window.navigationBarColor = Color.rgb(8, 13, 22)

        assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)
            settings.setSupportZoom(false)
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest) =
                    assetLoader.shouldInterceptRequest(request.url)
                        ?: super.shouldInterceptRequest(view, request)
            }
            overScrollMode = View.OVER_SCROLL_NEVER
            setBackgroundColor(Color.rgb(8, 13, 22))
        }
        setContentView(webView)
        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    override fun onPause() { if (::webView.isInitialized) webView.onPause(); super.onPause() }
    override fun onResume() { super.onResume(); if (::webView.isInitialized) webView.onResume() }
    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.stopLoading(); webView.loadUrl("about:blank"); webView.removeAllViews(); webView.destroy()
        }
        super.onDestroy()
    }
}
