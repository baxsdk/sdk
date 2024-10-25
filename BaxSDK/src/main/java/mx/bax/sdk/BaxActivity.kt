package mx.bax.sdk

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class BaxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("URL") ?: ""
        val apiKey = intent.getStringExtra("API_KEY")

        setContentView(R.layout.basic_webview)

        var webView: WebView = findViewById(R.id.simple_webview)
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true

            apiKey?.let { key ->
                val headers = mapOf("Authorization" to key)
                loadUrl(url, headers)
            }
        }
    }

    override fun finish() {
        super.finish()
    }
}