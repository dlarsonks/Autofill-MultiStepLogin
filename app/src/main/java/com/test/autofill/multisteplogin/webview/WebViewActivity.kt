package com.test.autofill.multisteplogin.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.test.autofill.databinding.WebviewLayoutBinding
import com.test.autofill.multisteplogin.util.applyInsetsPaddingIgnoreBottom
import com.test.autofill.multisteplogin.util.setNavigationBarContrastNotEnforced

class WebViewActivity : AppCompatActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, WebViewActivity::class.java)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = WebviewLayoutBinding.inflate(layoutInflater)

        binding.webview.loadUrl("https://facebook.com")
        val webSettings: WebSettings = binding.webview.settings
        binding.webview.webViewClient = WebViewClient()
        webSettings.javaScriptEnabled = true

        setContentView(binding.root)

        setNavigationBarContrastNotEnforced()
        applyInsetsPaddingIgnoreBottom(binding.root)
    }

}
