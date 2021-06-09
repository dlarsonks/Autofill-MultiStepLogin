package com.test.autofill.multisteplogin.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.databinding.OtpLayoutBinding
import com.test.autofill.multisteplogin.DoneActivity

/**
 * Created by dlarson at 6/9/21
 */
class OtpActivity : AppCompatActivity() {
    companion object {
        private val TAG = OtpActivity::class.java.simpleName

        fun createIntent(context: Context): Intent {
            return Intent(context, OtpActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = OtpLayoutBinding.inflate(layoutInflater)
        binding.submitButton.setOnClickListener { showDoneScreen() }
        setContentView(binding.root)
    }

    private fun showDoneScreen() {
        if (DEBUG) Log.d(TAG, "showDoneScreen: ")
        val intent = DoneActivity.createIntent(this)
        startActivity(intent)
        finish()
    }

}
