package com.test.autofill.multisteplogin.tapjacking

import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayOpacity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TapJackingOverlayOpacityTest {
    @Test
    fun `default opacity is fifty percent`() {
        assertThat(TapJackingOverlayOpacity.DEFAULT, equalTo(0.5f))
        assertThat(
            TapJackingOverlayOpacity.toPercentage(TapJackingOverlayOpacity.DEFAULT),
            equalTo(50),
        )
    }

    @Test
    fun `opacity is clamped to the slider range`() {
        assertThat(TapJackingOverlayOpacity.normalize(-1f), equalTo(0f))
        assertThat(TapJackingOverlayOpacity.normalize(2f), equalTo(1f))
    }

    @Test
    fun `non-finite opacity falls back to the default`() {
        assertThat(
            TapJackingOverlayOpacity.normalize(Float.NaN),
            equalTo(TapJackingOverlayOpacity.DEFAULT),
        )
    }
}
