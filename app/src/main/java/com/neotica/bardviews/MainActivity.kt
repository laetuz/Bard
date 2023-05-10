package com.neotica.bardviews

import android.annotation.SuppressLint
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.neotica.bardviews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingWeb()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun bindingWeb() {
        binding.apply {
            webView.loadUrl("https://bard.google.com/")
            webView.webViewClient = MyWebViewClient()
            webView.settings.javaScriptEnabled = true
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed() // Ignore SSL certificate errors
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            // Inject JavaScript code to force dark mode
            val darkModeScript = """
    (function() {
        var css = 'body { background-color: #333; color: #fff; }'; // Example dark mode styles
        css += 'h1, h2, h3, h4, h5, h6, p, span { color: #fff; }'; // Text color styles
        css += 'input[type="text"] { color: #fff; }'; // Text color style for input fields
        
        // Listen for text input events
        document.addEventListener('input', function(event) {
            var target = event.target;
            if (target.tagName.toLowerCase() === 'input' && target.type === 'text') {
                target.style.color = '#fff'; // Set the text color to white
            }
        }, false);

        // Apply initial styles
        var style = document.createElement('style');
        style.appendChild(document.createTextNode(css));
        document.head.appendChild(style);
    })()
""".trimIndent()


            view?.evaluateJavascript(darkModeScript, null)
        }
    }
}

