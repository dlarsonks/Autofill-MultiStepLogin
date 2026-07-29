package com.test.autofill.multisteplogin.tapjacking.basic

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TapJackingScreen(
    canDrawOverlays: Boolean,
    overlayShown: Boolean,
    overlayAlpha: Float,
    passThroughTouches: Boolean,
    decoyBias: Float,
    onRequestOverlayPermission: () -> Unit,
    onAlphaChange: (Float) -> Unit,
    onPassThroughChange: (Boolean) -> Unit,
    onDecoyBiasChange: (Float) -> Unit,
    onShowOverlay: () -> Unit,
    onHideOverlay: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Tap-jacking Test") }) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OverlayControls(
                canDrawOverlays = canDrawOverlays,
                overlayShown = overlayShown,
                overlayAlpha = overlayAlpha,
                passThroughTouches = passThroughTouches,
                decoyBias = decoyBias,
                onRequestOverlayPermission = onRequestOverlayPermission,
                onAlphaChange = onAlphaChange,
                onPassThroughChange = onPassThroughChange,
                onDecoyBiasChange = onDecoyBiasChange,
                onShowOverlay = onShowOverlay,
                onHideOverlay = onHideOverlay,
            )

            HorizontalDivider()

            TriggerInfo()

            HorizontalDivider()

            SelfCheck()
        }
    }
}

@Composable
private fun OverlayControls(
    canDrawOverlays: Boolean,
    overlayShown: Boolean,
    overlayAlpha: Float,
    passThroughTouches: Boolean,
    decoyBias: Float,
    onRequestOverlayPermission: () -> Unit,
    onAlphaChange: (Float) -> Unit,
    onPassThroughChange: (Boolean) -> Unit,
    onDecoyBiasChange: (Float) -> Unit,
    onShowOverlay: () -> Unit,
    onHideOverlay: () -> Unit,
) {
    Text("Overlay", style = MaterialTheme.typography.titleMedium)

    if (!canDrawOverlays) {
        Text(
            "\"Draw over other apps\" permission is not granted.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Button(onClick = onRequestOverlayPermission) {
            Text("Grant overlay permission")
        }
    } else {
        Text(
            "Overlay permission granted.",
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    Text("Opacity: ${(overlayAlpha * 100).toInt()}% (Android 12+ blocks pass-through at ≥ 80%)")
    Slider(
        value = overlayAlpha,
        onValueChange = onAlphaChange,
        valueRange = 0f..1f,
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(checked = passThroughTouches, onCheckedChange = onPassThroughChange)
        Text(
            "Pass touches through (FLAG_NOT_TOUCHABLE)",
            modifier = Modifier.padding(start = 12.dp),
        )
    }
    Text(
        if (passThroughTouches) {
            "On: taps fall through to Keeper underneath (the tap-jack). Position the decoy over " +
                "Keeper's Fill / LINK RECORD button so tapping it lands on that control."
        } else {
            "Off: the decoy captures taps itself (shows a Toast) instead of reaching Keeper."
        },
        style = MaterialTheme.typography.bodySmall,
    )

    Text("Decoy vertical position: ${(decoyBias * 100).toInt()}% (bottom ≈ Fill, middle ≈ LINK RECORD)")
    Slider(
        value = decoyBias,
        onValueChange = onDecoyBiasChange,
        valueRange = 0f..1f,
    )

    if (overlayShown) {
        Button(onClick = onHideOverlay, modifier = Modifier.fillMaxWidth()) {
            Text("Hide overlay")
        }
    } else {
        Button(
            onClick = onShowOverlay,
            enabled = canDrawOverlays,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Show overlay")
        }
    }
}

@Composable
private fun TriggerInfo() {
    Text("Trigger Keeper", style = MaterialTheme.typography.titleMedium)
    Text(
        "Enter a web domain and tap \"Load login form\" in the WebView below, then focus a field " +
            "to summon Keeper. The WebView reports that domain on its username/password ViewNodes " +
            "(via loadDataWithBaseURL), so Keeper matches a record for it and shows the search " +
            "screen with Fill; an unrecognized domain still shows the \"cannot validate\" disclaimer.",
        style = MaterialTheme.typography.bodyMedium,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SelfCheck() {
    var result by remember { mutableStateOf("Tap the box below with the overlay up.") }
    var acceptedTaps by remember { mutableIntStateOf(0) }

    Text("Obscured-touch self-check", style = MaterialTheme.typography.titleMedium)
    Text(
        "This reads the incoming MotionEvent flags — exactly what Keeper's windows should " +
            "check. With the overlay shown, taps here should be refused.",
        style = MaterialTheme.typography.bodyMedium,
    )

    Text(result, style = MaterialTheme.typography.bodyLarge)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFFE0E0E0))
            .pointerInteropFilter { event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val obscured = event.flags and MotionEvent.FLAG_WINDOW_IS_OBSCURED != 0
                    val partiallyObscured =
                        event.flags and MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED != 0
                    result = when {
                        obscured ->
                            "REFUSED — FLAG_WINDOW_IS_OBSCURED set"
                        partiallyObscured ->
                            "REFUSED — FLAG_WINDOW_IS_PARTIALLY_OBSCURED set"
                        else -> {
                            acceptedTaps++
                            "ACCEPTED — not obscured (taps: $acceptedTaps)"
                        }
                    }
                }
                true
            },
        contentAlignment = Alignment.Center,
    ) {
        Text("Tap here")
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TapJackingScreenPreview() {
    AutofillMultistepTheme {
        TapJackingScreen(
            canDrawOverlays = true,
            overlayShown = false,
            overlayAlpha = 0.5f,
            passThroughTouches = true,
            decoyBias = 0.85f,
            onRequestOverlayPermission = { },
            onAlphaChange = { },
            onPassThroughChange = { },
            onDecoyBiasChange = { },
            onShowOverlay = { },
            onHideOverlay = { },
        )
    }
}
