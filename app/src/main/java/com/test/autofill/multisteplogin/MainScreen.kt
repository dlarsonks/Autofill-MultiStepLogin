package com.test.autofill.multisteplogin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme

/**
 * Created by dlarson on 6/19/26
 */
enum class NavItem(val label: String) {
    BASIC_MULTI_STEP_LOGIN("Basic Multi-step Login"),
    PASSWORD_ONLY_LOGIN("Password-Only Login"),
    TWO_PASSWORD_FIELDS("Multi-step Login with Two Password Fields"),
    EXTRA_SCREEN_AFTER_USERNAME("Multi-step Login with an Extra Screen Between Username and Password"),
    EXTRA_SCREEN_AFTER_PASSWORD("Multi-step Login with an Extra Screen at the End of the Flow"),
    EXTRA_SCREEN_BEFORE_USERNAME("Multi-step Login with an Extra Screen at the Start of the Flow"),
    USERNAME_PASSWORD_SAME_SCREEN("Username (label, no hint) & Password"),
    PAYMENT_CARD("Payment Card"),
    ADDRESS("Address"),
    TWO_FACTOR_CODE("Two-Factor Code"),
    TWO_FACTOR_CODE_MULTI("Two-Factor Code Multi"),
    OTP_USERNAME_PASSWORD("OTP, Username, Password"),
    WEBVIEW("WebView"),
    PASSWORD_NOT_VISIBLE("Password Not Visible"),
    CUSTOM_TAB("Custom Tab"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onNavClick: (NavItem) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Autofill Examples") })
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NavItem.entries.forEach { item ->
                    Card(
                        onClick = { onNavClick(item) },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = item.label,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun MainScreenPreview() {
    AutofillMultistepTheme {
        MainScreen(onNavClick = { })
    }
}
