package com.test.autofill.multisteplogin.username

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.databinding.UsernameLayoutBinding

/**
 * Created by dlarson on 10/27/17.
 */
class UsernameFragment :  Fragment() {
    companion object {
        val TAG = UsernameFragment::class.simpleName
        const val REQUEST_KEY_USERNAME_ENTERED = "request_key_username_entered"

        fun newInstance() : Fragment {
            return UsernameFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = UsernameLayoutBinding.inflate(inflater)
        binding.submitButton.setOnClickListener { submitClicked() }
        return binding.root
    }

    private fun submitClicked() {
        if (DEBUG) Log.d(TAG, "submitClicked: ")
        parentFragmentManager.setFragmentResult(REQUEST_KEY_USERNAME_ENTERED, Bundle())
    }
}
