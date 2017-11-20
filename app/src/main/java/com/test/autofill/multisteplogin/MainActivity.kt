package com.test.autofill.multisteplogin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.test.autofill.R
import com.test.autofill.multisteplogin.navigation.NavigationItem
import kotlinx.android.synthetic.main.activity_main.*

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

        val multiStepLoginWithExtraScreenAfterUsername: NavigationItem = findViewById(R.id.multiStepLoginWithExtraScreenAfterUsername)
        multiStepLoginWithExtraScreenAfterUsername.setNavigationButtonClickListener { _: View ->
            launchMultiStepLoginWithExtraScreenAfterUsername()
        }

        val multiStepLoginWithExtraScreenInFlow: NavigationItem = findViewById(R.id.multiStepLoginWithExtraScreenAfterPassword)
        multiStepLoginWithExtraScreenInFlow.setNavigationButtonClickListener { _: View ->
            launchMultiStepLoginWithExtraScreenAfterPassword()
        }

        val multiStepLoginWithExtraScreenBeforeUsername: NavigationItem = findViewById(R.id.multiStepLoginWithExtraScreenBeforeUsername)
        multiStepLoginWithExtraScreenBeforeUsername.setNavigationButtonClickListener { _: View ->
            launchMultiStepLoginWithExtraScreenBeforeUsername()
        }
    }

    private fun launchBasicMultiStepLogin() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithTwoPasswordFields() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 2,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithExtraScreenAfterUsername() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = true,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithExtraScreenAfterPassword() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = true,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithExtraScreenBeforeUsername() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = true)
        startActivity(intent)
    }


}