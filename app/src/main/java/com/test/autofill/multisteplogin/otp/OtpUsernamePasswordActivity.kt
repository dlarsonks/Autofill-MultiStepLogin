package com.test.autofill.multisteplogin.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.databinding.OtpUsernamePasswordLayoutBinding
import com.test.autofill.multisteplogin.DoneActivity
import com.test.autofill.multisteplogin.util.applyInsetsPaddingIgnoreBottom
import com.test.autofill.multisteplogin.util.logD
import com.test.autofill.multisteplogin.util.setNavigationBarContrastNotEnforced

/**
 * Created by dlarson at 10/18/22
 */
class OtpUsernamePasswordActivity : AppCompatActivity() {
    companion object {
        private val TAG = OtpUsernamePasswordActivity::class.java.simpleName

        fun createIntent(context: Context): Intent {
            return Intent(context, OtpUsernamePasswordActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        logD { "onCreate: " }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = OtpUsernamePasswordLayoutBinding.inflate(layoutInflater)
        binding.submitButton.setOnClickListener { showDoneScreen() }

        setContentView(binding.root)

        setNavigationBarContrastNotEnforced()
        applyInsetsPaddingIgnoreBottom(binding.root)
    }

    private fun showDoneScreen() {
        if (DEBUG) Log.d(TAG, "showDoneScreen: ")
        val intent = DoneActivity.createIntent(this)
        startActivity(intent)
        finish()
    }

}
