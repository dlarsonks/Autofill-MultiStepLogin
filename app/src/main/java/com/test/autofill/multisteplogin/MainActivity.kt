package com.test.autofill.multisteplogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        basicMultiStepLogin.setNavigationButtonClickListener{
            launchBasicMultiStepLogin()
        }
        
        val passwordOnlyLogin: NavigationItem = findViewById(R.id.passwordOnlyLogin)
        passwordOnlyLogin.setNavigationButtonClickListener {
            launchPasswordOnlyLogin()
        }

        val multiStepTwoPasswordFields: NavigationItem = findViewById(R.id.multiStepLoginWithTwoPasswordFields)
        multiStepTwoPasswordFields.setNavigationButtonClickListener {
            launchMultiStepLoginWithTwoPasswordFields()
        }

        val multiStepLoginWithExtraScreenAfterUsername: NavigationItem = findViewById(R.id.multiStepLoginWithExtraScreenAfterUsername)
        multiStepLoginWithExtraScreenAfterUsername.setNavigationButtonClickListener {
            launchMultiStepLoginWithExtraScreenAfterUsername()
        }

        val multiStepLoginWithExtraScreenInFlow: NavigationItem = findViewById(R.id.multiStepLoginWithExtraScreenAfterPassword)
        multiStepLoginWithExtraScreenInFlow.setNavigationButtonClickListener {
            launchMultiStepLoginWithExtraScreenAfterPassword()
        }

        val multiStepLoginWithExtraScreenBeforeUsername: NavigationItem = findViewById(R.id.multiStepLoginWithExtraScreenBeforeUsername)
        multiStepLoginWithExtraScreenBeforeUsername.setNavigationButtonClickListener {
            launchMultiStepLoginWithExtraScreenBeforeUsername()
        }
    }

    private fun launchBasicMultiStepLogin() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                showUsernameScreen = true,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchPasswordOnlyLogin() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                showUsernameScreen = false,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }
    
    private fun launchMultiStepLoginWithTwoPasswordFields() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                showUsernameScreen = true,
                numberOfPasswordFields = 2,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithExtraScreenAfterUsername() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                showUsernameScreen = true,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = true,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithExtraScreenAfterPassword() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                showUsernameScreen = true,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = true,
                showExtraScreenBeforeUsername = false)
        startActivity(intent)
    }

    private fun launchMultiStepLoginWithExtraScreenBeforeUsername() {
        val intent = MultiLoginActivity.createIntent(
                context = this,
                showUsernameScreen = true,
                numberOfPasswordFields = 1,
                showExtraScreenAfterUsername = false,
                showExtraScreenAfterPassword = false,
                showExtraScreenBeforeUsername = true)
        startActivity(intent)
    }

}
