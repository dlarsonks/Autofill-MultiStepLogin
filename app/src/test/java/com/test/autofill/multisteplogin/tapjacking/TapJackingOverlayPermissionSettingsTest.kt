package com.test.autofill.multisteplogin.tapjacking

import com.test.autofill.multisteplogin.tapjacking.service.OverlayPermissionSettingsDestination
import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayPermissionSettings
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TapJackingOverlayPermissionSettingsTest {
    @Test
    fun `Android 10 uses the app-specific overlay settings screen`() {
        val destination = TapJackingOverlayPermissionSettings.destinationForSdk(sdkInt = 29)

        assertThat(
            destination,
            equalTo(OverlayPermissionSettingsDestination.OVERLAY_PERMISSION),
        )
    }

    @Test
    fun `Android 11 uses App Info because overlay settings ignore the package`() {
        val destination = TapJackingOverlayPermissionSettings.destinationForSdk(sdkInt = 30)

        assertThat(
            destination,
            equalTo(OverlayPermissionSettingsDestination.APP_DETAILS),
        )
    }

    @Test
    fun `newer Android versions use App Info`() {
        val destination = TapJackingOverlayPermissionSettings.destinationForSdk(sdkInt = 37)

        assertThat(
            destination,
            equalTo(OverlayPermissionSettingsDestination.APP_DETAILS),
        )
    }
}
