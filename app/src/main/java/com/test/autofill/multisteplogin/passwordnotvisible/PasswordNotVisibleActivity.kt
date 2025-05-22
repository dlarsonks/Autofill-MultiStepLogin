package com.test.autofill.multisteplogin.passwordnotvisible

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.test.autofill.databinding.PasswordNotVisibleToggleBinding
import com.test.autofill.multisteplogin.DoneActivity
import com.test.autofill.multisteplogin.dataStore
import com.test.autofill.multisteplogin.util.applyInsetsPaddingIgnoreBottom
import com.test.autofill.multisteplogin.util.logD
import com.test.autofill.multisteplogin.util.setNavigationBarContrastNotEnforced
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Created by dlarson on 6/18/24
 */
class PasswordNotVisibleActivity : AppCompatActivity() {
    companion object {
        private val TAG = PasswordNotVisibleActivity::class.java.simpleName

        fun createIntent(context: Context): Intent {
            return Intent(context, PasswordNotVisibleActivity::class.java)
        }
    }

    private val passwordVisiblePreferencesKey: Preferences.Key<Boolean> =
        booleanPreferencesKey("PASSWORD_VISIBLE_TOGGLE_SETTING")

    override fun onCreate(savedInstanceState: Bundle?) {
        logD { "onCreate: " }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = PasswordNotVisibleToggleBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            val preferences: Preferences? = dataStore.data.firstOrNull()
            logD(TAG) { "onCreate: preferences = $preferences" }

            preferences ?: return@launch

            val passwordVisiblePrefValue = preferences[passwordVisiblePreferencesKey]
                ?: return@launch

            if (passwordVisiblePrefValue) {
                binding.radioPasswordVisible.setChecked(true)
                binding.passwordEditText.isVisible = true
            } else {
                binding.radioPasswordNotVisible.setChecked(true)
                binding.passwordEditText.isInvisible = true
            }

        }

        binding.submitButton.setOnClickListener { showDoneScreen() }

        binding.radioPasswordVisible.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.passwordEditText.isVisible = true
                savePasswordVisiblePreference(true)
            }
        }

        binding.radioPasswordNotVisible.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
//                binding.passwordEditText.isVisible = false
                binding.passwordEditText.isInvisible = true
                savePasswordVisiblePreference(false)
            }
        }

        setContentView(binding.root)

        setNavigationBarContrastNotEnforced()
        applyInsetsPaddingIgnoreBottom(binding.root)
    }

    private fun showDoneScreen() {
        logD { "showDoneScreen: " }

        val intent = DoneActivity.createIntent(this)
        startActivity(intent)
        finish()
    }

    private fun savePasswordVisiblePreference(passwordVisible: Boolean) {
        logD { "savePasswordVisiblePreference: $passwordVisible" }

        lifecycleScope.launch {
            dataStore.edit { settings ->
                settings[passwordVisiblePreferencesKey] = passwordVisible
            }
        }
    }

}
