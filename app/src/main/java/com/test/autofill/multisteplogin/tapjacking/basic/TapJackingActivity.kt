package com.test.autofill.multisteplogin.tapjacking.basic

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import com.test.autofill.R
import com.test.autofill.databinding.TapjackingActivityBinding
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme
import com.test.autofill.multisteplogin.util.logD
import com.test.autofill.multisteplogin.util.setNavigationBarContrastNotEnforced
import com.test.autofill.multisteplogin.webview.MaliciousLoginPage

/**
 * Test screen for exercising Keeper's Autofill tap-jacking defenses. Draws a configurable
 * TYPE_APPLICATION_OVERLAY on top of everything (including Keeper's disclaimer dialog and
 * "choose a record to fill" screen) so we can verify Keeper drops obscured touches.
 */
class TapJackingActivity : ComponentActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, TapJackingActivity::class.java)
        }
    }

    private lateinit var binding: TapjackingActivityBinding
    private val overlayWindowManager by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }
    private var overlayView: View? = null

    private val canDrawOverlays = mutableStateOf(false)
    private val overlayShown = mutableStateOf(false)
    private val overlayAlpha = mutableFloatStateOf(0.5f)
    private val passThroughTouches = mutableStateOf(true)
    // 0f = decoy at top of screen, 1f = decoy at bottom
    private val decoyBias = mutableFloatStateOf(0.85f)

    override fun onCreate(savedInstanceState: Bundle?) {
        logD { "onCreate: " }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = TapjackingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // the WebView must stay a real view (not a Compose AndroidView) so the platform traverses
        // it for autofill and reports its webDomain/hints to Keeper
        binding.triggerWebview.webViewClient = WebViewClient()
        binding.domainInput.setText(MaliciousLoginPage.reportedDomain)
        binding.loadButton.setOnClickListener { loadTriggerPage() }
        loadTriggerPage()

        binding.controlsCompose.setContent {
            AutofillMultistepTheme {
                TapJackingScreen(
                    canDrawOverlays = canDrawOverlays.value,
                    overlayShown = overlayShown.value,
                    overlayAlpha = overlayAlpha.floatValue,
                    passThroughTouches = passThroughTouches.value,
                    decoyBias = decoyBias.floatValue,
                    onRequestOverlayPermission = ::requestOverlayPermission,
                    onAlphaChange = ::onAlphaChange,
                    onPassThroughChange = ::onPassThroughChange,
                    onDecoyBiasChange = ::onDecoyBiasChange,
                    onShowOverlay = ::showOverlay,
                    onHideOverlay = ::hideOverlay,
                )
            }
        }

        setNavigationBarContrastNotEnforced()
    }

    private fun loadTriggerPage() {
        val domain = binding.domainInput.text.toString()
        binding.triggerWebview.loadDataWithBaseURL(
            domain,
            MaliciousLoginPage.html,
            "text/html",
            "UTF-8",
            null,
        )
    }

    override fun onResume() {
        super.onResume()
        canDrawOverlays.value = Settings.canDrawOverlays(this)
    }

    private fun requestOverlayPermission() {
        logD { "requestOverlayPermission: " }
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.fromParts("package", packageName, null),
        )
        startActivity(intent)
    }

    private fun onAlphaChange(alpha: Float) {
        overlayAlpha.floatValue = alpha
        overlayView?.let { overlayWindowManager.updateViewLayout(it, buildLayoutParams()) }
    }

    private fun onPassThroughChange(passThrough: Boolean) {
        logD { "onPassThroughChange: $passThrough" }
        passThroughTouches.value = passThrough
        overlayView?.let { overlayWindowManager.updateViewLayout(it, buildLayoutParams()) }
    }

    private fun onDecoyBiasChange(bias: Float) {
        decoyBias.floatValue = bias
        positionDecoy()
    }

    private fun positionDecoy() {
        val root = overlayView ?: return
        val decoy = root.findViewById<Button>(R.id.decoyButton)
        // wait for layout so root/button heights are known, then offset from the top by the bias
        root.post {
            val travel = (root.height - decoy.height).coerceAtLeast(0)
            decoy.translationY = decoyBias.floatValue * travel
        }
    }

    private fun showOverlay() {
        logD { "showOverlay: " }
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
            return
        }
        if (overlayView != null) return

        val view = layoutInflater.inflate(R.layout.tapjacking_overlay, null)
        // fires only when the overlay is intercepting touches (FLAG_NOT_TOUCHABLE off); when it
        // passes touches through, this never runs and the tap lands on the obscured window below
        view.findViewById<Button>(R.id.decoyButton).setOnClickListener {
            Toast.makeText(this, "Decoy captured your tap", Toast.LENGTH_SHORT).show()
        }
        overlayWindowManager.addView(view, buildLayoutParams())
        overlayView = view
        overlayShown.value = true
        positionDecoy()
    }

    private fun hideOverlay() {
        logD { "hideOverlay: " }
        overlayView?.let { overlayWindowManager.removeViewImmediate(it) }
        overlayView = null
        overlayShown.value = false
    }

    private fun buildLayoutParams(): WindowManager.LayoutParams {
        var flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        if (passThroughTouches.value) {
            flags = flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        }

        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            flags,
            PixelFormat.TRANSLUCENT,
        ).apply {
            alpha = overlayAlpha.floatValue
        }
    }

    override fun onDestroy() {
        hideOverlay()
        super.onDestroy()
    }
}
