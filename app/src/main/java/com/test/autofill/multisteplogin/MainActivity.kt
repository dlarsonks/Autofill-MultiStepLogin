package com.test.autofill.multisteplogin

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.test.autofill.multisteplogin.compose.AutofillMultistepTheme
import com.test.autofill.multisteplogin.customtab.CustomTabActivity
import com.test.autofill.multisteplogin.otp.OtpActivity
import com.test.autofill.multisteplogin.otp.OtpMultiActivity
import com.test.autofill.multisteplogin.otp.OtpUsernamePasswordActivity
import com.test.autofill.multisteplogin.passwordnotvisible.PasswordNotVisibleActivity
import com.test.autofill.multisteplogin.username_and_password.UsernamePasswordTogetherActivity
import com.test.autofill.multisteplogin.webview.WebViewActivity
import com.test.autofill.multisteplogin.util.logD

/**
 * Created by dlarson on 11/1/17.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        logD { "onCreate: " }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AutofillMultistepTheme {
                MainScreen(onNavClick = ::onNavItemClicked)
            }
        }
    }

    private fun onNavItemClicked(navItem: NavItem) {
        when (navItem) {
            NavItem.BASIC_MULTI_STEP_LOGIN -> launchBasicMultiStepLogin()
            NavItem.PASSWORD_ONLY_LOGIN -> launchPasswordOnlyLogin()
            NavItem.TWO_PASSWORD_FIELDS -> launchMultiStepLoginWithTwoPasswordFields()
            NavItem.EXTRA_SCREEN_AFTER_USERNAME -> launchMultiStepLoginWithExtraScreenAfterUsername()
            NavItem.EXTRA_SCREEN_AFTER_PASSWORD -> launchMultiStepLoginWithExtraScreenAfterPassword()
            NavItem.EXTRA_SCREEN_BEFORE_USERNAME -> launchMultiStepLoginWithExtraScreenBeforeUsername()
            NavItem.USERNAME_PASSWORD_SAME_SCREEN -> launchUsernameAndPasswordOnSameScreen()
            NavItem.PAYMENT_CARD -> launchPaymentCardScreen()
            NavItem.ADDRESS -> launchAddressScreen()
            NavItem.TWO_FACTOR_CODE -> launchTwoFactorCodeScreen()
            NavItem.TWO_FACTOR_CODE_MULTI -> launchTwoFactorCodeScreenMulti()
            NavItem.OTP_USERNAME_PASSWORD -> launchOtpUsernamePasswordScreen()
            NavItem.WEBVIEW -> launchWebViewScreen()
            NavItem.PASSWORD_NOT_VISIBLE -> passwordNotVisibleToggleScreen()
            NavItem.CUSTOM_TAB -> launchCustomTabScreen()
        }
    }

    private fun launchBasicMultiStepLogin() {
        val intent = MultiLoginActivity.createIntent(
            context = this,
            showUsernameScreen = true,
            numberOfPasswordFields = 1,
            showExtraScreenAfterUsername = false,
            showExtraScreenAfterPassword = false,
            showExtraScreenBeforeUsername = false
        )
        startActivity(intent)
    }

    private fun launchPasswordOnlyLogin() {
        val intent = MultiLoginActivity.createIntent(
            context = this,
            showUsernameScreen = false,
            numberOfPasswordFields = 1,
            showExtraScreenAfterUsername = false,
            showExtraScreenAfterPassword = false,
            showExtraScreenBeforeUsername = false
        )
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

    private fun launchUsernameAndPasswordOnSameScreen() {
        val intent = UsernamePasswordTogetherActivity.createIntent(this)
        startActivity(intent)
    }

    private fun launchPaymentCardScreen() {
        val intent = MainProfileActivity.createShowPaymentCardIntent(this)
        startActivity(intent)
    }

    private fun launchAddressScreen() {
        val intent = MainProfileActivity.createShowAddressIntent(this)
        startActivity(intent)
    }

    private fun launchTwoFactorCodeScreen() {
        val intent = OtpActivity.createIntent(this)
        startActivity(intent)
    }

    private fun launchTwoFactorCodeScreenMulti() {
        val intent = OtpMultiActivity.createIntent(this)
        startActivity(intent)
    }

    private fun launchOtpUsernamePasswordScreen() {
        val intent = OtpUsernamePasswordActivity.createIntent(this)
        startActivity(intent)
    }

    private fun launchWebViewScreen() {
        val intent = WebViewActivity.createIntent(this)
        startActivity(intent)
    }

    private fun passwordNotVisibleToggleScreen() {
        val intent = PasswordNotVisibleActivity.createIntent(this)
        startActivity(intent)
    }

    private fun launchCustomTabScreen() {
        val intent = CustomTabActivity.createIntent(this)
        startActivity(intent)
    }

}
