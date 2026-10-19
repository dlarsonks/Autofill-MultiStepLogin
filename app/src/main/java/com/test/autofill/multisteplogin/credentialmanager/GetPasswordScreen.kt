package com.test.autofill.multisteplogin.credentialmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.autofill.R
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme
import com.test.autofill.multisteplogin.compose.ProgressDialog
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetPasswordScreen(
    uiStateFlow: StateFlow<GetPasswordUiState>,
    usernameState: TextFieldState,
    passwordState: TextFieldState,
    backClicked: () -> Unit,
    onSubmitClicked: () -> Unit,
    onClearFormClicked: () -> Unit,
) {
    val uiState = uiStateFlow.collectAsStateWithLifecycle()

    if (uiState.value.showProgress) {
        ProgressDialog()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Credential Manager - Get Password") },
                navigationIcon = {
                    IconButton(onClick = backClicked) {
                        Icon(
                            painterResource(R.drawable.outline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            MainContent(
                modifier = Modifier.padding(innerPadding),
                uiState = uiState.value,
                usernameState = usernameState,
                passwordState = passwordState,
                onSubmitClicked = onSubmitClicked,
                onClearFormClicked = onClearFormClicked,
            )
        },
    )
}

@Composable
private fun MainContent(
    modifier: Modifier,
    uiState: GetPasswordUiState,
    usernameState: TextFieldState,
    passwordState: TextFieldState,
    onSubmitClicked: () -> Unit,
    onClearFormClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val focusRequester = remember { FocusRequester() }

        Username(
            usernameState = usernameState,
            focusRequester = focusRequester,
        )

        Password(
            passwordState = passwordState,
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onSubmitClicked) {
                Text("Submit")
            }

            Spacer(Modifier.width(16.dp))

            Button(onClick = {
                focusRequester.requestFocus()
                onClearFormClicked()
            }) {
                Text("Clear")
            }
        }

        ErrorMessage(uiState.errorMessage)
    }
}

@Composable
private fun Username(
    usernameState: TextFieldState,
    focusRequester: FocusRequester,
) {
    OutlinedTextField(
        modifier = Modifier.focusRequester(focusRequester),
        state = usernameState,
        lineLimits = TextFieldLineLimits.SingleLine,
        label = { Text("Username") },
    )

}

@Composable
private fun Password(
    passwordState: TextFieldState,
) {
    OutlinedTextField(
        state = passwordState,
        lineLimits = TextFieldLineLimits.SingleLine,
        label = { Text("Password") },
    )
}

@Composable
private fun ErrorMessage(errorMessage: String?) {
    errorMessage ?: return

    Spacer(Modifier.height(16.dp))

    OutlinedCard(
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = Color(0xFFFF7361),
            contentColor = Color.Black
        ),
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = errorMessage,
            fontSize = 12.sp,
            style = TextStyle(fontFamily = FontFamily.Monospace)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreatePasswordCredentialScreenPreview(
    @PreviewParameter(GetPasswordCredentialUiStateProvider::class)
    uiState: GetPasswordUiState
) {
    AutofillMultistepTheme {
        GetPasswordScreen(
            uiStateFlow = MutableStateFlow(uiState),
            usernameState = rememberTextFieldState(),
            passwordState = rememberTextFieldState(),
            backClicked = { },
            onSubmitClicked = { },
            onClearFormClicked = { },
        )
    }
}

internal class GetPasswordCredentialUiStateProvider :
    PreviewParameterProvider<GetPasswordUiState> {

    val emptyState = GetPasswordUiState()

    val errorMessage = GetPasswordUiState(
        errorMessage = "This is a bunch of text representing an error message"
    )

    val showProgress = GetPasswordUiState(showProgress = true)


    override val values: Sequence<GetPasswordUiState>
        get() = sequenceOf(
            emptyState,
            errorMessage,
            showProgress
        )
}
