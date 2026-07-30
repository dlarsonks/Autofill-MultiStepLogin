package com.test.autofill.multisteplogin.tapjacking.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.test.autofill.R
import com.test.autofill.multisteplogin.util.logD
import com.test.autofill.multisteplogin.util.logE
import com.test.autofill.multisteplogin.util.logW
import kotlin.math.roundToInt

internal object TapJackingOverlayServiceStatus {
    var isRunning by mutableStateOf(false)
        private set

    var overlayOpacity = TapJackingOverlayOpacity.DEFAULT
        private set

    var overlayVerticalPosition = 0f
        private set

    fun markStarted() {
        isRunning = true
    }

    fun markStopped() {
        isRunning = false
        overlayOpacity = TapJackingOverlayOpacity.DEFAULT
        overlayVerticalPosition = 0f
    }

    fun updateOverlayConfiguration(
        opacity: Float,
        verticalPosition: Float,
    ) {
        overlayOpacity = TapJackingOverlayOpacity.normalize(opacity)
        overlayVerticalPosition = TapJackingOverlayPosition.normalize(verticalPosition)
    }
}

internal object TapJackingOverlayPosition {
    fun normalize(verticalPosition: Float): Float {
        return if (verticalPosition.isFinite()) {
            verticalPosition.coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    fun calculateYOffset(
        screenHeightPx: Int,
        overlayHeightPx: Int,
        verticalPosition: Float,
    ): Int {
        val availableHeightPx = (screenHeightPx - overlayHeightPx).coerceAtLeast(0)
        return (availableHeightPx * normalize(verticalPosition)).roundToInt()
    }

    fun toPercentage(verticalPosition: Float): Int {
        return (normalize(verticalPosition) * 100).roundToInt()
    }
}

internal object TapJackingOverlayOpacity {
    const val DEFAULT = 0.5f

    fun normalize(opacity: Float): Float {
        return if (opacity.isFinite()) {
            opacity.coerceIn(0f, 1f)
        } else {
            DEFAULT
        }
    }

    fun toPercentage(opacity: Float): Int {
        return (normalize(opacity) * 100).roundToInt()
    }
}

/**
 * adds a TYPE_APPLICATION_OVERLAY strip over whatever is on screen. must be installed under a
 * different package than the probe, because the platform does not report obscuration by windows
 * sharing an application token or uid.
 *
 * the strip's geometry can be varied per start with the heightDp, yDp and gravity extras, which
 * matters when the control under test would otherwise sit beneath the strip: the strip is touchable,
 * so it swallows taps inside its own bounds instead of letting them reach the app below.
 *
 * ```
 * adb shell am start-foreground-service -n com.test.autofill/com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayService
 * adb shell am start-foreground-service -n com.test.autofill/com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayService --ei heightDp 60 --ei yDp 400 --es gravity top
 *```
 * To stop the service click the Stop Service button, or:
 * ```
 *  adb shell am force-stop com.test.autofill
 *  ```
 */
class TapJackingOverlayService : Service() {
    companion object {

        private const val CHANNEL_ID = "tj_overlay"
        private const val DEFAULT_HEIGHT_DP = 100

        private const val EXTRA_HEIGHT_DP = "heightDp"
        private const val EXTRA_Y_DP = "yDp"
        private const val EXTRA_GRAVITY = "gravity"
        private const val EXTRA_VERTICAL_POSITION = "verticalPosition"
        private const val EXTRA_OPACITY = "opacity"

        internal fun createIntent(
            context: Context,
            verticalPosition: Float,
            opacity: Float,
        ): Intent {
            return Intent(context, TapJackingOverlayService::class.java).apply {
                putExtra(
                    EXTRA_VERTICAL_POSITION,
                    TapJackingOverlayPosition.normalize(verticalPosition),
                )
                putExtra(EXTRA_OPACITY, TapJackingOverlayOpacity.normalize(opacity))
            }
        }

        private fun intExtra(intent: Intent?, name: String, fallback: Int): Int {
            return intent?.getIntExtra(name, fallback) ?: fallback
        }
    }

    private var overlay: View? = null

    override fun onCreate() {
        super.onCreate()
        TapJackingOverlayServiceStatus.markStarted()
    }

    /**
     * the overlay is applied here rather than in onCreate because onCreate runs only once per
     * service instance. with the work there, a repeated `am start-foreground-service` reached only
     * onStartCommand and so did nothing at all, which on the device is indistinguishable from the
     * overlay permission having been denied. re-running the start command now re-applies the strip.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startAsForegroundService()
        removeOverlay()
        addOverlay(intent)
        return START_STICKY
    }

    private fun startAsForegroundService() {
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID, "overlay", NotificationManager.IMPORTANCE_LOW
            )
        )

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("TJ overlay active")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        startForeground(1, notification)
    }

    private fun addOverlay(intent: Intent?) {
        val density = resources.displayMetrics.density
        val heightPx = (intExtra(intent, EXTRA_HEIGHT_DP, DEFAULT_HEIGHT_DP) * density).toInt()
        val verticalPosition = intent?.takeIf {
            it.hasExtra(EXTRA_VERTICAL_POSITION)
        }?.getFloatExtra(EXTRA_VERTICAL_POSITION, 0f)
        val yPx = if (verticalPosition != null) {
            TapJackingOverlayPosition.calculateYOffset(
                screenHeightPx = resources.displayMetrics.heightPixels,
                overlayHeightPx = heightPx,
                verticalPosition = verticalPosition,
            )
        } else {
            (intExtra(intent, EXTRA_Y_DP, 0) * density).toInt()
        }
        val alignBottom = verticalPosition == null &&
                intent != null &&
                "bottom".equals(intent.getStringExtra(EXTRA_GRAVITY), ignoreCase = true)
        val opacity = TapJackingOverlayOpacity.normalize(
            intent?.getFloatExtra(EXTRA_OPACITY, TapJackingOverlayOpacity.DEFAULT)
                ?: TapJackingOverlayOpacity.DEFAULT,
        )

        val view = TextView(this)
        view.text = getString(R.string.untrusted_service_overlay)
        view.setTextColor(Color.WHITE)
        view.setBackgroundColor(Color.RED)
        view.setGravity(Gravity.CENTER)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            heightPx,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.OPAQUE
        )
        params.gravity = (if (alignBottom) Gravity.BOTTOM else Gravity.TOP) or Gravity.START
        params.y = yPx
        params.alpha = opacity

        try {
            windowManager()!!.addView(view, params)
            overlay = view
            TapJackingOverlayServiceStatus.updateOverlayConfiguration(
                opacity = opacity,
                verticalPosition = verticalPosition ?: 0f,
            )

            logD { "addOverlay: overlay added " +
                    "sdk=${Build.VERSION.SDK_INT}, " +
                    "pkg=$packageName heightPx=$heightPx, " +
                    "yPx=$yPx opacity=$opacity, " +
                    "gravity=${if (alignBottom) "bottom" else "top"}" }
        } catch (e: Exception) {
            logE(e) { "addOverlay: overlay add failed" }
        }
    }

    private fun removeOverlay() {
        if (overlay == null) {
            return
        }
        try {
            windowManager()!!.removeView(overlay)
            logD { "removeOverlay: overlay removed" }
        } catch (e: Exception) {
            logW(e) { "removeOverlay: overlay remove failed" }
        }
        overlay = null
    }

    private fun windowManager(): WindowManager? {
        return getSystemService(WINDOW_SERVICE) as WindowManager?
    }

    override fun onDestroy() {
        removeOverlay()
        TapJackingOverlayServiceStatus.markStopped()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
