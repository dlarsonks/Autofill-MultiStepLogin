package com.test.autofill.multisteplogin.first_last_name

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.databinding.FirstLastNameLayoutBinding

/**
 * Created by dlarson on 10/27/17.
 */
class FirstAndLastNameFragment :  Fragment() {

    companion object {
        val TAG = FirstAndLastNameFragment::class.simpleName
        const val REQUEST_KEY_NAME_ENTERED = "request_key_name_entered"

        fun newInstance() : Fragment {
            return FirstAndLastNameFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FirstLastNameLayoutBinding.inflate(inflater)
        binding.submitButton.setOnClickListener { submitClicked() }
        return binding.root
    }

    private fun submitClicked() {
        if (DEBUG) Log.d(TAG, "submitClicked: ")
        parentFragmentManager.setFragmentResult(REQUEST_KEY_NAME_ENTERED, Bundle())
    }
}