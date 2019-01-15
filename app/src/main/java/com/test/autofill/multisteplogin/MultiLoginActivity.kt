package com.test.autofill.multisteplogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.multisteplogin.password.PasswordFragment
import com.test.autofill.R
import com.test.autofill.multisteplogin.first_last_name.FirstAndLastNameEnteredCallback
import com.test.autofill.multisteplogin.first_last_name.FirstAndLastNameFragment
import com.test.autofill.multisteplogin.password.PasswordEnteredCallback
import com.test.autofill.multisteplogin.username.UsernameEnteredCallback
import com.test.autofill.multisteplogin.username.UsernameFragment

class MultiLoginActivity : AppCompatActivity(),
        UsernameEnteredCallback,
        PasswordEnteredCallback,
        FirstAndLastNameEnteredCallback {

    private var showUsernameScreen: Boolean = true
    private var numberOfPasswordFields: Int = 1
    private var showExtraScreenAfterUsername: Boolean = false
    private var showExtraScreenAfterPassword: Boolean = false
    private var showExtraScreenBeforeUsername: Boolean = false

    companion object {
        val TAG = MultiLoginActivity::class.simpleName

        private const val SHOW_USERNAME_SCREEN = "show_username_screen_extra"
        private const val NUMBER_OF_PASSWORD_FIELDS = "number_of_password_fields_extra"
        private const val EXTRA_SCREEN_AFTER_USERNAME = "extra_screen_after_username_extra"
        private const val EXTRA_SCREEN_AFTER_PASSWORD = "extra_screen_after_password_extra"
        private const val EXTRA_SCREEN_BEFORE_USERNAME = "extra_screen_before_username_extra"

        fun createIntent(
                context: Context,
                showUsernameScreen: Boolean = true,
                numberOfPasswordFields: Int,
                showExtraScreenAfterUsername: Boolean,
                showExtraScreenAfterPassword: Boolean,
                showExtraScreenBeforeUsername: Boolean): Intent {
            val intent = Intent(context, MultiLoginActivity::class.java)
            intent.putExtra(SHOW_USERNAME_SCREEN, showUsernameScreen)
            intent.putExtra(NUMBER_OF_PASSWORD_FIELDS, numberOfPasswordFields)
            intent.putExtra(EXTRA_SCREEN_AFTER_PASSWORD, showExtraScreenAfterPassword)
            intent.putExtra(EXTRA_SCREEN_AFTER_USERNAME, showExtraScreenAfterUsername)
            intent.putExtra(EXTRA_SCREEN_BEFORE_USERNAME, showExtraScreenBeforeUsername)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)

        val extras = intent.extras
        if (extras != null) {
            showUsernameScreen = extras.getBoolean(SHOW_USERNAME_SCREEN, true)
            numberOfPasswordFields = extras.getInt(NUMBER_OF_PASSWORD_FIELDS, 1)
            showExtraScreenAfterUsername = extras.getBoolean(EXTRA_SCREEN_AFTER_USERNAME, false)
            showExtraScreenAfterPassword = extras.getBoolean(EXTRA_SCREEN_AFTER_PASSWORD, false)
            showExtraScreenBeforeUsername = extras.getBoolean(EXTRA_SCREEN_BEFORE_USERNAME, false)
        }

        when {
            showExtraScreenBeforeUsername -> showFirstAndLastNameScreen()
            showUsernameScreen -> showUsernameScreen()
            else -> showPasswordScreen()
        }
    }

    private fun showUsernameScreen() {
        if (DEBUG) Log.d(TAG, "showUsernameScreen: ")
        val fragment = UsernameFragment.newInstance()
        showFragment(fragment = fragment, tag = "UsernameFragment")
    }

    override fun usernameEntered() {
        if (DEBUG) Log.d(TAG, "usernameEntered: ")
        if(showExtraScreenAfterUsername) {
            showFirstAndLastNameScreen()
        } else {
            showPasswordScreen()
        }
    }

    override fun passwordEntered() {
        if (DEBUG) Log.d(TAG, "passwordEntered: ")
        if (showExtraScreenAfterPassword) {
            showFirstAndLastNameScreen()
        } else {
            showDoneScreen()
        }
    }

    override fun firstAndLastNameEntered() {
        if (DEBUG) Log.d(TAG, "firstAndLastNameEntered: ")
        when {
            showExtraScreenBeforeUsername -> showUsernameScreen()
            showExtraScreenAfterUsername -> showPasswordScreen()
            else -> showDoneScreen()
        }
    }

    private fun showFirstAndLastNameScreen() {
        if (DEBUG) Log.d(TAG, "showFirstAndLastNameScreen: ")
        val fragment = FirstAndLastNameFragment.newInstance()
        showFragment(fragment = fragment, tag = "FirstAndLastNameFragment")
    }

    private fun showDoneScreen() {
        if (DEBUG) Log.d(TAG, "showDoneScreen: ")
        val intent = Intent(this, DoneActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showPasswordScreen() {
        if (DEBUG) Log.d(TAG, "showPasswordScreen: ")
        val fragment = PasswordFragment.newInstance(numberOfPasswordFields)
        showFragment(fragment = fragment, tag = "PasswordFragment")
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment, tag)
        transaction.commit()
    }
}
