package com.test.autofill.multisteplogin.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.test.autofill.databinding.MaliciousWebviewLayoutBinding
import com.test.autofill.multisteplogin.util.applyInsetsPaddingIgnoreBottom
import com.test.autofill.multisteplogin.util.logD
import com.test.autofill.multisteplogin.util.setNavigationBarContrastNotEnforced

class MaliciousWebViewActivity : AppCompatActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MaliciousWebViewActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        logD { "onCreate: " }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = MaliciousWebviewLayoutBinding.inflate(layoutInflater)
        binding.domainSummary.text = MaliciousLoginPage.domainSummary

        with(binding.webview) {
            webViewClient = WebViewClient()
            loadDataWithBaseURL(
                MaliciousLoginPage.reportedDomain,
                MaliciousLoginPage.html,
                "text/html",
                "UTF-8",
                null,
            )
        }

        setContentView(binding.root)

        setNavigationBarContrastNotEnforced()
        applyInsetsPaddingIgnoreBottom(binding.root)
    }

}

object MaliciousLoginPage {
    const val reportedDomain = "https://m.facebook.com"
    const val sourceUrl = "https://attacker.com/"
    val domainSummary = "Real URL: $sourceUrl\nReported URL: $reportedDomain"

    val html =
        """
        <!doctype html>
        <html lang="en">
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Sign in</title>
            <style>
                body {
                    font-family: sans-serif;
                    margin: 0;
                    padding: 32px 24px;
                }
                form {
                    display: grid;
                    gap: 16px;
                    max-width: 360px;
                }
                input,
                button {
                    box-sizing: border-box;
                    font: inherit;
                    padding: 12px;
                    width: 100%;
                }
            </style>
        </head>
        <body>
            <main>
                <h1>Sign in</h1>
                <form>
                    <label for="username">Username</label>
                    <input
                        autocomplete="username"
                        id="username"
                        name="username"
                        type="text">

                    <label for="password">Password</label>
                    <input
                        autocomplete="current-password"
                        id="password"
                        name="password"
                        type="password">

                    <button type="button">Continue</button>
                </form>
            </main>
        </body>
        </html>
        """.trimIndent()
}
