package com.test.autofill.multisteplogin.customtab

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme
import com.test.autofill.multisteplogin.util.logD
import kotlinx.coroutines.launch
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

/**
 * Created by dlarson on 9/3/25
 */
class CustomTabActivity : ComponentActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, CustomTabActivity::class.java)
        }
    }

    private val viewModel: CustomTabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AutofillMultistepTheme {
                CustomTabScreen(
                    url = viewModel.url,
                    openOnClick = viewModel::openUrlInCustomTab,
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEvents.collect { uiEvent: CustomTabViewEvent? ->
                    uiEvent ?: return@collect

                    when (uiEvent) {
                        is CustomTabViewEvent.CustomTabToastEvent -> {
                            Toast.makeText(this@CustomTabActivity, uiEvent.toastMessage, Toast.LENGTH_SHORT).show()
                        }
                        is CustomTabViewEvent.CustomTabOpenUrlEvent -> {
                            launchUrl(uiEvent.url)
                        }
                    }
                }
            }
        }

    }

    private fun launchUrl(url: String) {
        logD { "launchUrl: $url" }

        val packageNameToUse: String? = CustomTabsHelper.getPackageNameToUse(this)
        logD { "launchUrl: packageNameToUse = $packageNameToUse" }

        if (packageNameToUse.isNullOrBlank()) {
            Toast.makeText(this, "No browser found that supports Custom Tabs", Toast.LENGTH_SHORT).show()
            return
        }

        val intentBuilder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val customTabsIntent: CustomTabsIntent = intentBuilder.build()

        customTabsIntent.intent.data = url.toUri()
        customTabsIntent.intent.setPackage(packageNameToUse)

        customTabsIntent.launchUrl(this, url.toUri())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Changing the theme doesn't recreate the activity, so set the E2E values again
        enableEdgeToEdge()
    }

}
