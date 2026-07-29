package com.test.autofill.multisteplogin.tapjacking

import com.test.autofill.multisteplogin.tapjacking.service.OverlayPermissionSettingsDestination
import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayOpacity
import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayServiceUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TapJackingOverlayServiceUiStateTest {
    @Test
    fun `overlay settings action is shown when permission is missing and settings are available`() {
        val state = TapJackingOverlayServiceUiState(
            canDrawOverlays = false,
            canOpenOverlaySettings = true,
            isServiceRunning = false,
            overlayPermissionSettingsDestination =
                OverlayPermissionSettingsDestination.OVERLAY_PERMISSION,
            overlayOpacity = TapJackingOverlayOpacity.DEFAULT,
            overlayVerticalPosition = 0f,
        )

        assertThat(state.showOverlaySettingsButton, equalTo(true))
    }

    @Test
    fun `overlay settings action is hidden when settings are unavailable`() {
        val state = TapJackingOverlayServiceUiState(
            canDrawOverlays = false,
            canOpenOverlaySettings = false,
            isServiceRunning = false,
            overlayPermissionSettingsDestination =
                OverlayPermissionSettingsDestination.OVERLAY_PERMISSION,
            overlayOpacity = TapJackingOverlayOpacity.DEFAULT,
            overlayVerticalPosition = 0f,
        )

        assertThat(state.showOverlaySettingsButton, equalTo(false))
    }

    @Test
    fun `start action is enabled only when permission is granted and service is stopped`() {
        val state = TapJackingOverlayServiceUiState(
            canDrawOverlays = true,
            canOpenOverlaySettings = true,
            isServiceRunning = false,
            overlayPermissionSettingsDestination =
                OverlayPermissionSettingsDestination.OVERLAY_PERMISSION,
            overlayOpacity = TapJackingOverlayOpacity.DEFAULT,
            overlayVerticalPosition = 0f,
        )

        assertThat(state.startServiceEnabled, equalTo(true))
        assertThat(state.stopServiceEnabled, equalTo(false))
    }

    @Test
    fun `stop action is enabled only while service is running`() {
        val state = TapJackingOverlayServiceUiState(
            canDrawOverlays = true,
            canOpenOverlaySettings = true,
            isServiceRunning = true,
            overlayPermissionSettingsDestination =
                OverlayPermissionSettingsDestination.OVERLAY_PERMISSION,
            overlayOpacity = TapJackingOverlayOpacity.DEFAULT,
            overlayVerticalPosition = 0f,
        )

        assertThat(state.startServiceEnabled, equalTo(false))
        assertThat(state.stopServiceEnabled, equalTo(true))
    }

    @Test
    fun `overlay position percentage reflects the slider position`() {
        val state = TapJackingOverlayServiceUiState(
            canDrawOverlays = true,
            canOpenOverlaySettings = true,
            isServiceRunning = false,
            overlayPermissionSettingsDestination =
                OverlayPermissionSettingsDestination.OVERLAY_PERMISSION,
            overlayOpacity = TapJackingOverlayOpacity.DEFAULT,
            overlayVerticalPosition = 0.5f,
        )

        assertThat(state.overlayVerticalPositionPercent, equalTo(50))
    }

    @Test
    fun `overlay opacity percentage reflects the slider opacity`() {
        val state = TapJackingOverlayServiceUiState(
            canDrawOverlays = true,
            canOpenOverlaySettings = true,
            isServiceRunning = false,
            overlayPermissionSettingsDestination =
                OverlayPermissionSettingsDestination.OVERLAY_PERMISSION,
            overlayOpacity = 0.75f,
            overlayVerticalPosition = 0f,
        )

        assertThat(state.overlayOpacityPercent, equalTo(75))
    }
}
