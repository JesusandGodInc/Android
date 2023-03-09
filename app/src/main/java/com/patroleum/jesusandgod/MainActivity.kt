package com.patroleum.jesusandgod

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_error.view.*


class MainActivity : AppCompatActivity() {

    private var _error = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.supportActionBar?.hide()

        web_view.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showError()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress_bar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showWebView()
            }
        }

        web_view.webChromeClient = MyWebChromeClient(this)
        web_view.settings.allowFileAccess = true
        web_view.settings.javaScriptEnabled = true

        val currentUrl = savedInstanceState?.getString(CURRENT_URL)
        if (currentUrl != null) {
            web_view.loadUrl(currentUrl)
        } else {
            web_view.loadUrl(LOAD_URL)
        }

        error_view.btn_retry.setOnClickListener {
            _error = false
            web_view.reload()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putString(CURRENT_URL, web_view.url)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showError() {
        _error = true
        error_view.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        web_view.visibility = View.GONE
    }

    private fun showWebView() {
        if (!_error) {
            error_view.visibility = View.GONE
            progress_bar.visibility = View.GONE
            web_view.visibility = View.VISIBLE
        }
    }


}