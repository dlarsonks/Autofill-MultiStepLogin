package com.test.autofill.multisteplogin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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


    val TAG = MultiLoginActivity::class.simpleName

    private var numberOfPasswordFields: Int = 1
    private var showExtraScreenAtEndOfFlow: Boolean = false

    companion object {
        private val NUMBER_OF_PASSWORD_FIELDS = "number_of_password_fields_extra"
        private val EXTRA_SCREEN_AT_END_OF_FLOW = "extra_screen_at_end_of_flow_extra"

        fun createIntent(
                context: Context,
                numberOfPasswordFields: Int,
                showExtraScreenAtEndOfFlow: Boolean): Intent {
            val intent = Intent(context, MultiLoginActivity::class.java)
            intent.putExtra(NUMBER_OF_PASSWORD_FIELDS, numberOfPasswordFields)
            intent.putExtra(EXTRA_SCREEN_AT_END_OF_FLOW, showExtraScreenAtEndOfFlow)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)

        numberOfPasswordFields = intent.extras.getInt(NUMBER_OF_PASSWORD_FIELDS, 1)
        showExtraScreenAtEndOfFlow = intent.extras.getBoolean(EXTRA_SCREEN_AT_END_OF_FLOW, false)

        showUsernameScreen()
    }

    private fun showUsernameScreen() {
        if (DEBUG) Log.d(TAG, "showUsernameScreen: ")
        val fragment = UsernameFragment.newInstance()
        showFragment(fragment = fragment, tag = "UsernameFragment")
    }

    override fun usernameEntered() {
        if (DEBUG) Log.d(TAG, "usernameEntered: ")
        showPasswordScreen()
    }

    override fun passwordEntered() {
        if (DEBUG) Log.d(TAG, "passwordEntered: ")
        if(showExtraScreenAtEndOfFlow) {
            showFirstAndLastNameScreen()
        } else {
            showDoneScreen()
        }
    }

    override fun firstAndLastNameEntered() {
        if (DEBUG) Log.d(TAG, "firstAndLastNameEntered: ")
        showDoneScreen()
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
