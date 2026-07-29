package com.test.autofill.multisteplogin.tapjacking

import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayServiceStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

class TapJackingOverlayServiceStatusTest {
    @After
    fun tearDown() {
        TapJackingOverlayServiceStatus.markStopped()
    }

    @Test
    fun `service status reports running after the service starts`() {
        TapJackingOverlayServiceStatus.markStopped()

        TapJackingOverlayServiceStatus.markStarted()

        assertThat(TapJackingOverlayServiceStatus.isRunning, equalTo(true))
    }

    @Test
    fun `service status reports stopped after the service stops`() {
        TapJackingOverlayServiceStatus.markStarted()

        TapJackingOverlayServiceStatus.markStopped()

        assertThat(TapJackingOverlayServiceStatus.isRunning, equalTo(false))
    }

    @Test
    fun `service status retains the active configuration for a reopened activity`() {
        TapJackingOverlayServiceStatus.markStarted()
        TapJackingOverlayServiceStatus.updateOverlayConfiguration(
            opacity = 0.65f,
            verticalPosition = 0.75f,
        )

        assertThat(TapJackingOverlayServiceStatus.overlayOpacity, equalTo(0.65f))
        assertThat(TapJackingOverlayServiceStatus.overlayVerticalPosition, equalTo(0.75f))
    }
}
