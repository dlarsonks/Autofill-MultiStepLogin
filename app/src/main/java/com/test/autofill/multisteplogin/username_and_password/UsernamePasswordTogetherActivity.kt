package com.test.autofill.multisteplogin.username_and_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.databinding.UsernameAndPasswordLayoutWLabelBinding
import com.test.autofill.multisteplogin.DoneActivity
import com.test.autofill.multisteplogin.MultiLoginActivity

class UsernamePasswordTogetherActivity : AppCompatActivity() {
    companion object {
        private val TAG = UsernamePasswordTogetherActivity::class.java.simpleName

        fun createIntent(context: Context): Intent {
            return Intent(context, UsernamePasswordTogetherActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val binding = UsernameAndPasswordLayoutBinding.inflate(layoutInflater)
        val binding = UsernameAndPasswordLayoutWLabelBinding.inflate(layoutInflater)
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
