package com.test.autofill.multisteplogin.customtab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.autofill.R
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme

/**
 * Created by dlarson on 9/3/25
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomTabScreen(
    url: MutableState<String>,
    openOnClick: () -> Unit,
) {

    Scaffold(
        topBar = { AppBar() },
        content = { innerPadding ->
            MainContent(
                modifier = Modifier.padding(innerPadding),
                url = url,
                openOnClick = openOnClick,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    TopAppBar(
        title = { Text("Custom Tabs") }
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    url: MutableState<String>,
    openOnClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "This is a placeholder for the Custom Tab Screen",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = url.value,
            onValueChange = { url.value = it },
            label = { Text(stringResource(R.string.url_for_custom_tab)) },
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = openOnClick,
            content = { Text("Open")}
        )

    }
}

@Preview
@Composable
private fun CustomTabScreenPreview() {
    AutofillMultistepTheme {
        CustomTabScreen(
            url = remember { mutableStateOf("") },
            openOnClick = { },
        )
    }
}
