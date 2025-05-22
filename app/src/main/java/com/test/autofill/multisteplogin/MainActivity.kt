package com.test.autofill.multisteplogin

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.test.autofill.databinding.ActivityMainBinding
import com.test.autofill.multisteplogin.otp.OtpActivity
import com.test.autofill.multisteplogin.otp.OtpMultiActivity
import com.test.autofill.multisteplogin.otp.OtpUsernamePasswordActivity
import com.test.autofill.multisteplogin.passwordnotvisible.PasswordNotVisibleActivity
import com.test.autofill.multisteplogin.username_and_password.UsernamePasswordTogetherActivity
import com.test.autofill.multisteplogin.util.applyInsetsPaddingIgnoreBottom
import com.test.autofill.multisteplogin.util.setNavigationBarContrastNotEnforced
import com.test.autofill.multisteplogin.webview.WebViewActivity
import com.test.autofill.multisteplogin.util.logD

/**
 * Created by dlarson on 11/1/17.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        logD { "onCreate: " }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationBarContrastNotEnforced()
        applyInsetsPaddingIgnoreBottom(binding.root)

        binding.basicMultiStepLogin.setNavigationButtonClickListener { launchBasicMultiStepLogin() }
        binding.passwordOnlyLogin.setNavigationButtonClickListener { launchPasswordOnlyLogin() }
        binding.multiStepLoginWithTwoPasswordFields.setNavigationButtonClickListener { launchMultiStepLoginWithTwoPasswordFields() }
        binding.multiStepLoginWithExtraScreenAfterUsername.setNavigationButtonClickListener { launchMultiStepLoginWithExtraScreenAfterUsername() }
        binding.multiStepLoginWithExtraScreenAfterPassword.setNavigationButtonClickListener { launchMultiStepLoginWithExtraScreenAfterPassword() }
        binding.multiStepLoginWithExtraScreenBeforeUsername.setNavigationButtonClickListener { launchMultiStepLoginWithExtraScreenBeforeUsername() }
        binding.usernameAndPasswordOnSameScreen.setNavigationButtonClickListener { launchUsernameAndPasswordOnSameScreen() }
        binding.paymentCard.setNavigationButtonClickListener { launchPaymentCardScreen() }
        binding.address.setNavigationButtonClickListener { launchAddressScreen() }
        binding.twoFactorCode.setNavigationButtonClickListener { launchTwoFactorCodeScreen() }
        binding.twoFactorCodeMulti.setNavigationButtonClickListener { launchTwoFactorCodeScreenMulti() }
        binding.otpUsernamePassword.setNavigationButtonClickListener { launchOtpUsernamePasswordScreen() }
        binding.webview.setNavigationButtonClickListener { launchWebViewScreen() }
        binding.passwordNotVisibleToggle.setNavigationButtonClickListener {
            passwordNotVisibleToggleScreen()
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

}
