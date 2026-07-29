package com.test.autofill.multisteplogin.tapjacking.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme
import com.test.autofill.multisteplogin.util.logD

internal data class TapJackingOverlayServiceActivityState(
    val opacity: Float,
    val verticalPosition: Float,
) {
    companion object {
        fun restore(
            opacity: Float,
            verticalPosition: Float,
        ): TapJackingOverlayServiceActivityState {
            return TapJackingOverlayServiceActivityState(
                opacity = TapJackingOverlayOpacity.normalize(opacity),
                verticalPosition = TapJackingOverlayPosition.normalize(verticalPosition),
            )
        }
    }
}

/**
 * logs the raw MotionEvent flags for every touch so we can see whether the platform sets
 * FLAG_WINDOW_IS_PARTIALLY_OBSCURED on a given API level.
 */
class TapJackingOverlayServiceActivity : ComponentActivity() {
    companion object {
        private const val STATE_OVERLAY_OPACITY = "overlayOpacity"
        private const val STATE_OVERLAY_VERTICAL_POSITION = "overlayVerticalPosition"

        fun createIntent(context: Context): Intent {
            return Intent(context, TapJackingOverlayServiceActivity::class.java)
        }
    }

    private val canDrawOverlays = mutableStateOf(false)
    private val overlayPermissionSettingsDestination =
        TapJackingOverlayPermissionSettings.destinationForSdk(Build.VERSION.SDK_INT)
    private val overlayOpacity = mutableFloatStateOf(TapJackingOverlayOpacity.DEFAULT)
    private val overlayVerticalPosition = mutableFloatStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreOverlayConfiguration(savedInstanceState)
        enableEdgeToEdge()

        val canOpenOverlaySettings =
            overlayPermissionSettingsIntent().resolveActivity(packageManager) != null

        setContent {
            AutofillMultistepTheme {
                TapJackingOverlayServiceScreen(
                    uiState = TapJackingOverlayServiceUiState(
                        canDrawOverlays = canDrawOverlays.value,
                        canOpenOverlaySettings = canOpenOverlaySettings,
                        isServiceRunning = TapJackingOverlayServiceStatus.isRunning,
                        overlayPermissionSettingsDestination =
                            overlayPermissionSettingsDestination,
                        overlayOpacity = overlayOpacity.floatValue,
                        overlayVerticalPosition = overlayVerticalPosition.floatValue,
                    ),
                    onOpenOverlaySettings = ::openOverlaySettings,
                    onOverlayOpacityChange = ::onOverlayOpacityChange,
                    onOverlayOpacityChangeFinished = ::onOverlayConfigurationChangeFinished,
                    onOverlayVerticalPositionChange = ::onOverlayVerticalPositionChange,
                    onOverlayVerticalPositionChangeFinished =
                        ::onOverlayConfigurationChangeFinished,
                    onStartService = ::startOverlayService,
                    onStopService = ::stopOverlayService,
                )
            }
        }

        logD { "onCreate: probe sdk=${Build.VERSION.SDK_INT} pkg=$packageName" }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat(STATE_OVERLAY_OPACITY, overlayOpacity.floatValue)
        outState.putFloat(
            STATE_OVERLAY_VERTICAL_POSITION,
            overlayVerticalPosition.floatValue,
        )
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        canDrawOverlays.value = Settings.canDrawOverlays(this)
    }

    private fun openOverlaySettings() {
        logD { "openOverlaySettings: " }
        val intent = overlayPermissionSettingsIntent()
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun startOverlayService() {
        logD { "startOverlayService: " }
        if (!Settings.canDrawOverlays(this)) {
            canDrawOverlays.value = false
            return
        }
        ContextCompat.startForegroundService(
            this,
            TapJackingOverlayService.createIntent(
                context = this,
                verticalPosition = overlayVerticalPosition.floatValue,
                opacity = overlayOpacity.floatValue,
            ),
        )
    }

    private fun stopOverlayService() {
        logD { "stopOverlayService: " }
        stopService(
            TapJackingOverlayService.createIntent(
                context = this,
                verticalPosition = overlayVerticalPosition.floatValue,
                opacity = overlayOpacity.floatValue,
            ),
        )
    }

    private fun onOverlayOpacityChange(opacity: Float) {
        overlayOpacity.floatValue = TapJackingOverlayOpacity.normalize(opacity)
    }

    private fun onOverlayVerticalPositionChange(verticalPosition: Float) {
        overlayVerticalPosition.floatValue =
            TapJackingOverlayPosition.normalize(verticalPosition)
    }

    private fun onOverlayConfigurationChangeFinished() {
        if (TapJackingOverlayServiceStatus.isRunning) {
            startOverlayService()
        }
    }

    private fun restoreOverlayConfiguration(savedInstanceState: Bundle?) {
        val restoredState = if (TapJackingOverlayServiceStatus.isRunning) {
            TapJackingOverlayServiceActivityState.restore(
                opacity = TapJackingOverlayServiceStatus.overlayOpacity,
                verticalPosition =
                    TapJackingOverlayServiceStatus.overlayVerticalPosition,
            )
        } else {
            savedInstanceState ?: return
            TapJackingOverlayServiceActivityState.restore(
                opacity = savedInstanceState.getFloat(
                    STATE_OVERLAY_OPACITY,
                    TapJackingOverlayOpacity.DEFAULT,
                ),
                verticalPosition = savedInstanceState.getFloat(
                    STATE_OVERLAY_VERTICAL_POSITION,
                    0f,
                ),
            )
        }
        overlayOpacity.floatValue = restoredState.opacity
        overlayVerticalPosition.floatValue = restoredState.verticalPosition
    }

    private fun overlayPermissionSettingsIntent(): Intent {
        val packageUri = Uri.fromParts("package", packageName, null)
        return when (overlayPermissionSettingsDestination) {
            OverlayPermissionSettingsDestination.OVERLAY_PERMISSION ->
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri)
            OverlayPermissionSettingsDestination.APP_DETAILS ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri)
        }
    }

}
