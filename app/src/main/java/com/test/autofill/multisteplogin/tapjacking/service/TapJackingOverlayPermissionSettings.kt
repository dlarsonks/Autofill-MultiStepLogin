package com.test.autofill.multisteplogin.tapjacking.service

import android.os.Build

internal enum class OverlayPermissionSettingsDestination {
    OVERLAY_PERMISSION,
    APP_DETAILS,
}

internal object TapJackingOverlayPermissionSettings {
    fun destinationForSdk(sdkInt: Int): OverlayPermissionSettingsDestination {
        return if (sdkInt < Build.VERSION_CODES.R) {
            OverlayPermissionSettingsDestination.OVERLAY_PERMISSION
        } else {
            OverlayPermissionSettingsDestination.APP_DETAILS
        }
    }
}
