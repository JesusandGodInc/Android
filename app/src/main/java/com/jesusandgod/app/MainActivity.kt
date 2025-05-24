package com.jesusandgod.app

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jesusandgod.app.R

class MainActivity : AppCompatActivity() {

    private var _error = false

    private lateinit var webView: WebView
    private lateinit var progressBar: View
    private lateinit var errorView: View
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        // Asignar vistas
        webView = findViewById(R.id.web_view)
        progressBar = findViewById(R.id.progress_bar)
        errorView = findViewById(R.id.error_view)
        retryButton = errorView.findViewById(R.id.btn_retry)

        // WebViewClient para manejar navegación
        webView.webViewClient = object : WebViewClient() {
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
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showWebView()
            }
        }

        // WebChromeClient para logs (opcional, útil en debug)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("WebViewConsole", consoleMessage?.message() ?: "")
                return super.onConsoleMessage(consoleMessage)
            }
        }

        // Configuraciones de la WebView
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            allowFileAccess = true
            allowContentAccess = true
            builtInZoomControls = false
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // Cargar URL
        val currentUrl = savedInstanceState?.getString(CURRENT_URL)
        if (currentUrl != null) {
            webView.loadUrl(currentUrl)
        } else {
            webView.loadUrl(LOAD_URL)
        }

        // Botón para recargar en caso de error
        retryButton.setOnClickListener {
            _error = false
            webView.reload()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_URL, webView.url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showError() {
        _error = true
        errorView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        webView.visibility = View.GONE
    }

    private fun showWebView() {
        if (!_error) {
            errorView.visibility = View.GONE
            progressBar.visibility = View.GONE
            webView.visibility = View.VISIBLE
        }
    }
}
