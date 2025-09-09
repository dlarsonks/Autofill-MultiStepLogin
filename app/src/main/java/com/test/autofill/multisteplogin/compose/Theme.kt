package com.test.autofill.multisteplogin.compose

// https://github.com/android/compose-samples/blob/main/JetLagged/app/src/main/java/com/example/jetlagged/ui/theme/Theme.kt

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val shapes: Shapes
    @Composable
    get() = MaterialTheme.shapes.copy(
        large = CircleShape,
    )
@Composable
fun AutofillMultistepTheme(content: @Composable () -> Unit) {

    val colorScheme = lightColorScheme(
        primary = Color(0xFF3F51B5),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFFFFC700),
        onSecondary = Color(0xFF000000),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF000000),
        onSurfaceVariant = Color(0xFFB3B3B3),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF000000),
        error = Color(0xFFFF7361),
        onError = Color(0xFF202124)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        content = content,
    )
}
