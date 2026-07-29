package com.test.autofill.multisteplogin.tapjacking

import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayPosition
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TapJackingOverlayPositionTest {
    @Test
    fun `top position has no vertical offset`() {
        val yOffset = TapJackingOverlayPosition.calculateYOffset(
            screenHeightPx = 1_000,
            overlayHeightPx = 200,
            verticalPosition = 0f,
        )

        assertThat(yOffset, equalTo(0))
    }

    @Test
    fun `middle position centers the overlay in the available vertical space`() {
        val yOffset = TapJackingOverlayPosition.calculateYOffset(
            screenHeightPx = 1_000,
            overlayHeightPx = 200,
            verticalPosition = 0.5f,
        )

        assertThat(yOffset, equalTo(400))
    }

    @Test
    fun `bottom position uses the full available vertical offset`() {
        val yOffset = TapJackingOverlayPosition.calculateYOffset(
            screenHeightPx = 1_000,
            overlayHeightPx = 200,
            verticalPosition = 1f,
        )

        assertThat(yOffset, equalTo(800))
    }

    @Test
    fun `positions outside the slider range are clamped`() {
        val aboveTop = TapJackingOverlayPosition.calculateYOffset(
            screenHeightPx = 1_000,
            overlayHeightPx = 200,
            verticalPosition = -1f,
        )
        val belowBottom = TapJackingOverlayPosition.calculateYOffset(
            screenHeightPx = 1_000,
            overlayHeightPx = 200,
            verticalPosition = 2f,
        )

        assertThat(aboveTop, equalTo(0))
        assertThat(belowBottom, equalTo(800))
    }

    @Test
    fun `non-finite position falls back to the top`() {
        val yOffset = TapJackingOverlayPosition.calculateYOffset(
            screenHeightPx = 1_000,
            overlayHeightPx = 200,
            verticalPosition = Float.NaN,
        )

        assertThat(yOffset, equalTo(0))
    }
}
