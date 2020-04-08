package com.test.autofill.multisteplogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.test.autofill.R
import com.test.autofill.databinding.ActivityMainBinding
import com.test.autofill.multisteplogin.username_and_password.UsernamePasswordTogetherActivity

/**
 * Created by dlarson on 11/1/17.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.basicMultiStepLogin.setNavigationButtonClickListener { launchBasicMultiStepLogin() }
        binding.passwordOnlyLogin.setNavigationButtonClickListener { launchPasswordOnlyLogin() }
        binding.multiStepLoginWithTwoPasswordFields.setNavigationButtonClickListener { launchMultiStepLoginWithTwoPasswordFields() }
        binding.multiStepLoginWithExtraScreenAfterUsername.setNavigationButtonClickListener { launchMultiStepLoginWithExtraScreenAfterUsername() }
        binding.multiStepLoginWithExtraScreenAfterPassword.setNavigationButtonClickListener { launchMultiStepLoginWithExtraScreenAfterPassword() }
        binding.multiStepLoginWithExtraScreenBeforeUsername.setNavigationButtonClickListener { launchMultiStepLoginWithExtraScreenBeforeUsername() }
        binding.usernameAndPasswordOnSameScreen.setNavigationButtonClickListener { launchUsernameAndPasswordOnSameScreen() }
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

    private fun launchUsernameAndPasswordOnSameScreen() {
        val intent = UsernamePasswordTogetherActivity.createIntent(this)
        startActivity(intent)
    }

}
