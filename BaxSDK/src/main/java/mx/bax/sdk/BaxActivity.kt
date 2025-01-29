package mx.bax.sdk

import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import mx.bax.sdk.BaxSDK.R

class BaxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("URL") ?: ""
        val token = intent.getStringExtra("TOKEN")

        setContentView(R.layout.basic_webview)

        var webView: WebView = findViewById(R.id.simple_webview)
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d(
                        "WebViewConsole",
                        "Line ${consoleMessage.lineNumber()}: ${consoleMessage.message()} (Source: ${consoleMessage.sourceId()})"
                    )
                    return super.onConsoleMessage(consoleMessage)
                }
            }

            token?.let { key ->
                val headers = mapOf("Authorization" to "Bearer $key")
                loadUrl(url, headers)
            }
        }
    }

    override fun finish() {
        super.finish()
    }
}