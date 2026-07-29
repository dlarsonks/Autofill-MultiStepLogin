package com.test.autofill.multisteplogin.tapjacking.service

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme

internal data class TapJackingOverlayServiceUiState(
    val canDrawOverlays: Boolean,
    val canOpenOverlaySettings: Boolean,
    val isServiceRunning: Boolean,
    val overlayPermissionSettingsDestination: OverlayPermissionSettingsDestination,
    val overlayOpacity: Float,
    val overlayVerticalPosition: Float,
) {
    val showOverlaySettingsButton: Boolean
        get() = !canDrawOverlays && canOpenOverlaySettings

    val startServiceEnabled: Boolean
        get() = canDrawOverlays && !isServiceRunning

    val stopServiceEnabled: Boolean
        get() = isServiceRunning

    val overlayOpacityPercent: Int
        get() = TapJackingOverlayOpacity.toPercentage(overlayOpacity)

    val overlayVerticalPositionPercent: Int
        get() = TapJackingOverlayPosition.toPercentage(overlayVerticalPosition)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TapJackingOverlayServiceScreen(
    uiState: TapJackingOverlayServiceUiState,
//    touchStatus: String,
    onOpenOverlaySettings: () -> Unit,
    onOverlayOpacityChange: (Float) -> Unit,
    onOverlayOpacityChangeFinished: () -> Unit,
    onOverlayVerticalPositionChange: (Float) -> Unit,
    onOverlayVerticalPositionChangeFinished: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Tap-jacking Overlay Service") }) },
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                ).verticalScroll(rememberScrollState()),
        ) {
            OverlayPermissionSection(
                uiState = uiState,
                onOpenOverlaySettings = onOpenOverlaySettings,
            )

            HorizontalDivider()

            OverlayServiceSection(
                uiState = uiState,
                onOverlayOpacityChange = onOverlayOpacityChange,
                onOverlayOpacityChangeFinished = onOverlayOpacityChangeFinished,
                onOverlayVerticalPositionChange = onOverlayVerticalPositionChange,
                onOverlayVerticalPositionChangeFinished =
                    onOverlayVerticalPositionChangeFinished,
                onStartService = onStartService,
                onStopService = onStopService,
            )

            HorizontalDivider()

//            TouchEventProbeSection(touchStatus = touchStatus)
        }
    }
}

@Composable
private fun OverlayPermissionSection(
    uiState: TapJackingOverlayServiceUiState,
    onOpenOverlaySettings: () -> Unit,
) {
    ListItem(
        headlineContent = { Text("Draw over other apps") },
        supportingContent = {
            Text(
                if (uiState.canDrawOverlays) {
                    "Permission granted"
                } else if (
                    uiState.overlayPermissionSettingsDestination ==
                    OverlayPermissionSettingsDestination.APP_DETAILS
                ) {
                    "Permission not granted. In App Info, tap \"Display over other apps\", " +
                        "then enable \"Allow display over other apps\"."
                } else {
                    "Permission not granted"
                },
            )
        },
    )

    if (!uiState.canDrawOverlays) {
        if (uiState.showOverlaySettingsButton) {
            Button(
                onClick = onOpenOverlaySettings,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    if (
                        uiState.overlayPermissionSettingsDestination ==
                        OverlayPermissionSettingsDestination.APP_DETAILS
                    ) {
                        "Open App Info"
                    } else {
                        "Open overlay settings"
                    },
                )
            }
        } else {
            ListItem(
                headlineContent = { Text("Overlay settings are unavailable") },
                supportingContent = {
                    Text("This device cannot open the app-specific overlay settings.")
                },
            )
        }
    }
}

@Composable
private fun OverlayServiceSection(
    uiState: TapJackingOverlayServiceUiState,
    onOverlayOpacityChange: (Float) -> Unit,
    onOverlayOpacityChangeFinished: () -> Unit,
    onOverlayVerticalPositionChange: (Float) -> Unit,
    onOverlayVerticalPositionChangeFinished: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
) {
    ListItem(
        headlineContent = { Text("Overlay service") },
        supportingContent = {
            Text(
                if (uiState.isServiceRunning) {
                    "Running"
                } else {
                    "Not running"
                },
            )
        },
    )

    OverlayOpacitySlider(
        opacity = uiState.overlayOpacity,
        opacityPercent = uiState.overlayOpacityPercent,
        onOpacityChange = onOverlayOpacityChange,
        onOpacityChangeFinished = onOverlayOpacityChangeFinished,
    )

    OverlayPositionSlider(
        verticalPosition = uiState.overlayVerticalPosition,
        verticalPositionPercent = uiState.overlayVerticalPositionPercent,
        onVerticalPositionChange = onOverlayVerticalPositionChange,
        onVerticalPositionChangeFinished = onOverlayVerticalPositionChangeFinished,
    )

    Button(
        onClick = onStartService,
        enabled = uiState.startServiceEnabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Start service")
    }

    OutlinedButton(
        onClick = onStopService,
        enabled = uiState.stopServiceEnabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Stop service")
    }
}

@Composable
private fun OverlayOpacitySlider(
    opacity: Float,
    opacityPercent: Int,
    onOpacityChange: (Float) -> Unit,
    onOpacityChangeFinished: () -> Unit,
) {
    Text("Opacity: $opacityPercent% (Android 12+ blocks pass-through at ≥ 80%)")
    Slider(
        value = opacity,
        onValueChange = onOpacityChange,
        modifier = Modifier.fillMaxWidth(),
        onValueChangeFinished = onOpacityChangeFinished,
        valueRange = 0f..1f,
    )
}

@Composable
private fun OverlayPositionSlider(
    verticalPosition: Float,
    verticalPositionPercent: Int,
    onVerticalPositionChange: (Float) -> Unit,
    onVerticalPositionChangeFinished: () -> Unit,
) {
    Text("Overlay vertical position: $verticalPositionPercent% (top to bottom)")
    Slider(
        value = verticalPosition,
        onValueChange = onVerticalPositionChange,
        modifier = Modifier.fillMaxWidth(),
        onValueChangeFinished = onVerticalPositionChangeFinished,
        valueRange = 0f..1f,
    )
}

@Composable
private fun TouchEventProbeSection(touchStatus: String) {
    ListItem(
        headlineContent = {
            Text(
                text = "Touch event probe",
                style = MaterialTheme.typography.titleMedium,
            )
        },
        supportingContent = { Text(touchStatus) },
    )
}

@Preview(showSystemUi = true)
@Composable
private fun TapJackingOverlayServiceScreenPreview() {
    AutofillMultistepTheme {
        TapJackingOverlayServiceScreen(
            uiState = TapJackingOverlayServiceUiState(
                canDrawOverlays = true,
                canOpenOverlaySettings = true,
                isServiceRunning = false,
                overlayPermissionSettingsDestination =
                    OverlayPermissionSettingsDestination.APP_DETAILS,
                overlayOpacity = TapJackingOverlayOpacity.DEFAULT,
                overlayVerticalPosition = 0.5f,
            ),
//            touchStatus = "Tap the middle of the screen",
            onOpenOverlaySettings = { },
            onOverlayOpacityChange = { },
            onOverlayOpacityChangeFinished = { },
            onOverlayVerticalPositionChange = { },
            onOverlayVerticalPositionChangeFinished = { },
            onStartService = { },
            onStopService = { },
        )
    }
}
