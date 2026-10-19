package com.test.autofill.multisteplogin.credentialmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetPasswordActivity : AppCompatActivity() {
    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, GetPasswordActivity::class.java)
        }
    }

    private val viewModel: GetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutofillMultistepTheme {
                GetPasswordScreen(
                    uiStateFlow = viewModel.uiState,
                    usernameState = viewModel.usernameState,
                    passwordState = viewModel.passwordState,
                    backClicked = {
                        finish()
                    },
                    onSubmitClicked = viewModel::getCredentialsClicked,
                    onClearFormClicked = viewModel::onClearFormClicked,
                )
            }
        }
    }

}
