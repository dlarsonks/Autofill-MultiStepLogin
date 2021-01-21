package com.test.autofill.multisteplogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.test.autofill.R
import com.test.autofill.multisteplogin.address.AddressEnteredCallback
import com.test.autofill.multisteplogin.address.AddressFragment
import com.test.autofill.multisteplogin.paymentcard.PaymentCardEnteredCallback
import com.test.autofill.multisteplogin.paymentcard.PaymentCardFragment

/**
 * Created by dlarson at 1/21/21
 */
class MainProfileActivity : AppCompatActivity(),
        PaymentCardEnteredCallback,
        AddressEnteredCallback {

    companion object {

        enum class ProfileScreen {
            PaymentCard, Address
        }

        private const val PROFILE_SCREEN_TYPE = "profile_screen_type"

        fun createShowPaymentCardIntent(context: Context): Intent {
            return Intent(context, MainProfileActivity::class.java).apply {
                putExtra(PROFILE_SCREEN_TYPE, ProfileScreen.PaymentCard)
            }
        }

        fun createShowAddressIntent(context: Context): Intent {
            return Intent(context, MainProfileActivity::class.java).apply {
                putExtra(PROFILE_SCREEN_TYPE, ProfileScreen.Address)
            }
        }

        private fun getProfileScreen(extras: Bundle): ProfileScreen? {
            return extras.getSerializable(PROFILE_SCREEN_TYPE) as ProfileScreen?
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)

        val extras = intent.extras ?: error("missing extras")

        @Suppress("MoveVariableDeclarationIntoWhen")
        val profileScreen = getProfileScreen(extras) ?: error("no screen type found")

        when (profileScreen) {
            ProfileScreen.PaymentCard -> showPaymentCardScreen()
            ProfileScreen.Address -> showAddressScreen()
        }
    }

    private fun showAddressScreen() {
        val fragment = AddressFragment.newInstance()
        showFragment(fragment, AddressFragment.TAG)
    }

    private fun showPaymentCardScreen() {
        val fragment = PaymentCardFragment.newInstance()
        showFragment(fragment, PaymentCardFragment.TAG)
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment, tag)
        transaction.commit()
    }

    override fun paymentCardEntered() {
        showDoneScreen()
    }

    private fun showDoneScreen() {
        val intent = Intent(this, DoneActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun addressEntered() {
        showDoneScreen()
    }
}