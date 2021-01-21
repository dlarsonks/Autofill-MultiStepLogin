package com.test.autofill.multisteplogin.first_last_name

import android.content.Context
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

        fun newInstance() : Fragment {
            return FirstAndLastNameFragment()
        }
    }

    private lateinit var firstAndLastNameEnteredCallback: FirstAndLastNameEnteredCallback

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FirstLastNameLayoutBinding.inflate(inflater)
        binding.submitButton.setOnClickListener { submitClicked() }
        return binding.root
    }

    private fun submitClicked() {
        if (DEBUG) Log.d(TAG, "submitClicked: ")
        firstAndLastNameEnteredCallback.firstAndLastNameEntered()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FirstAndLastNameEnteredCallback) {
            firstAndLastNameEnteredCallback = context
        }
    }
}