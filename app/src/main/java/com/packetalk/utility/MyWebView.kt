package com.packetalk.utility

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.webkit.*
import com.packetalk.util.NetworkUtils

class MyWebView : WebView {

    constructor(context: Context) : super(context) {
        initDefaultSetting()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initDefaultSetting()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initDefaultSetting()
    }

    constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initDefaultSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initDefaultSetting() {
        val webSettings = this.settings
        webSettings.javaScriptEnabled = true
      //  webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
//        webSettings.useWideViewPort = true
//        webSettings.loadWithOverviewMode = true

        webChromeClient = WebChromeClient()
        webViewClient = MyWebViewClient()
    }

    /**
     * Load Web View with url
     */
    fun load(url: String, context: Context) {
        if (NetworkUtils.isOnline(context)) {
            this.loadUrl(url)
        } else {
            this.loadUrl(url)
        }
    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun onReceivedHttpAuthRequest(
            view: WebView,
            handler: HttpAuthHandler, host: String, realm: String
        ) {
            handler.proceed("XMLdata", "Jigish")
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            view.visibility = View.INVISIBLE
            view.setBackgroundColor(Color.BLACK)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
//            view.visibility = View.VISIBLE
            view.setBackgroundColor(0)
        }
    }
}
