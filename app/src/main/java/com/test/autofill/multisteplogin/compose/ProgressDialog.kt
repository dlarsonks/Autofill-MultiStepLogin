package com.test.autofill.multisteplogin.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ProgressDialog(
    onDismissRequest: () -> Unit = { },
    properties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = { /* Handle dismiss if allowed */ },
        properties = DialogProperties(
            dismissOnBackPress = false, // Prevents closing with back button
            dismissOnClickOutside = false // Prevents closing when tapping background
        )
    ) {
        // Box container to style the dialog window
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
