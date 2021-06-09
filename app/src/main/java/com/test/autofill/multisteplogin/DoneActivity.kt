package com.test.autofill.multisteplogin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.test.autofill.R

@SuppressLint("Registered")
/**
 * Created by dlarson on 10/27/17.
 */
class DoneActivity : AppCompatActivity(R.layout.done_layout) {
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, DoneActivity::class.java)
        }
    }
}
