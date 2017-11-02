package com.test.autofill.multisteplogin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.test.autofill.R
import com.test.autofill.multisteplogin.navigation.NavigationItem

/**
 * Created by dlarson on 11/1/17.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val basicMultiStepLogin: NavigationItem = findViewById(R.id.basicMultiStepLogin)
        basicMultiStepLogin.setNavigationButtonClickListener{ _: View ->
            launchBasicMultiStepLogin()
        }

        val multiStepTwoPasswordFields: NavigationItem = findViewById(R.id.multiStepLoginWithTwoPasswordFields)
        multiStepTwoPasswordFields.setNavigationButtonClickListener { _: View ->
            launchMultiStepLoginWithTwoPasswordFields()
        }

        val multiStepLoginWithExtraScreenInFlow: NavigationItem = findViewById(R.id.multiStepLoginWithExtraScreenInFlow)
        multiStepLoginWithExtraScreenInFlow.setNavigationButtonClickListener { _: View ->
            launchMultiStepLoginWithExtraScreenInFlow()
        }
    }

    private fun launchBasicMultiStepLogin() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 1,
                showExtraScreenAtEndOfFlow = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithTwoPasswordFields() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 2,
                showExtraScreenAtEndOfFlow = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithExtraScreenInFlow() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 1,
                showExtraScreenAtEndOfFlow = true)
        startActivity(intent)

    }
}