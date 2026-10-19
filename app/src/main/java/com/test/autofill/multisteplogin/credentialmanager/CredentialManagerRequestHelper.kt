package com.test.autofill.multisteplogin.credentialmanager

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CredentialManagerRequestHelper @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun getPasswords(allowList: Set<String>): GetCredentialResponse {

        val getPasswordOption = GetPasswordOption(allowedUserIds = allowList)
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(getPasswordOption)
            .build()

        return credentialManager.getCredential(
            context = context,
            credentialRequest
        )
    }
}