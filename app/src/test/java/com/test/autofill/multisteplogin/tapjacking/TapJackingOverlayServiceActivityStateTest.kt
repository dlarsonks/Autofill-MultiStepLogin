package com.test.autofill.multisteplogin.tapjacking

import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayOpacity
import com.test.autofill.multisteplogin.tapjacking.service.TapJackingOverlayServiceActivityState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TapJackingOverlayServiceActivityStateTest {
    @Test
    fun `restored state preserves the overlay configuration`() {
        val state = TapJackingOverlayServiceActivityState.restore(
            opacity = 0.65f,
            verticalPosition = 0.75f,
        )

        assertThat(state.opacity, equalTo(0.65f))
        assertThat(state.verticalPosition, equalTo(0.75f))
    }

    @Test
    fun `restored state normalizes invalid overlay configuration`() {
        val state = TapJackingOverlayServiceActivityState.restore(
            opacity = Float.NaN,
            verticalPosition = 2f,
        )

        assertThat(state.opacity, equalTo(TapJackingOverlayOpacity.DEFAULT))
        assertThat(state.verticalPosition, equalTo(1f))
    }
}
