package com.test.autofill.multisteplogin.credentialmanager

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.autofill.multisteplogin.util.logD
import com.test.autofill.multisteplogin.util.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GetPasswordViewModel @Inject constructor(
    private val requestHelper: CredentialManagerRequestHelper,
): ViewModel() {

    companion object {
        private val TAG = GetPasswordViewModel::class.java.simpleName
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logE(TAG, throwable) { "" }
    }

    private val _uiState = MutableStateFlow(
        GetPasswordUiState()
    )

    private var getCredentialsJob: Job? = null

    val uiState: StateFlow<GetPasswordUiState> = _uiState.asStateFlow()

    val usernameState: TextFieldState = TextFieldState()
    val passwordState = TextFieldState()

    fun getCredentialsClicked() {
        val username = usernameState.text.toString()
        logD { "getCredentialsClicked: username = $username" }

        if (getCredentialsJob?.isActive == true) {
            logD { "getCredentialsClicked: get credentials job already active" }
            return
        }

        _uiState.update { it.copy(errorMessage = null) }

        getCredentialsJob = viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {

                val allowList = if (username.isBlank()) {
                    emptySet()
                } else {
                    setOf(username)
                }

                try {
                    val getCredentialResponse = requestHelper.getPasswords(allowList)

                    val credential = getCredentialResponse.credential
                    if (credential is PasswordCredential) {
                        logD(TAG) { "getCredentialsClicked: credential.id = ${credential.id}" }
                        logD(TAG) { "getCredentialsClicked: credential.password = ${credential.password}" }

                        usernameState.clearText()
                        passwordState.clearText()

                        usernameState.edit { append(credential.id) }
                        passwordState.edit { append(credential.password) }
                    }
                } catch (e: Exception) {
                    handleFailure(e)
                }
            }
        }
    }

    private fun handleFailure(e: Exception) {
        logE(e) { "" }

        if (e is NoCredentialException) {
            _uiState.update { it.copy(errorMessage = e.message) }
        }
    }

    fun onClearFormClicked() {
        usernameState.clearText()
        passwordState.clearText()
    }
}

data class GetPasswordUiState(
    val showProgress: Boolean = false,
    val errorMessage: String? = null,
)
