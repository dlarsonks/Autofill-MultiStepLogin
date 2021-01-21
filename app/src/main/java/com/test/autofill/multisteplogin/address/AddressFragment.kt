package com.test.autofill.multisteplogin.address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.autofill.databinding.AddressLayoutBinding

/**
 * Created by dlarson at 1/21/21
 */
class AddressFragment : Fragment() {
    companion object {
        val TAG: String = AddressFragment::class.java.simpleName
        fun newInstance(): AddressFragment {
            return AddressFragment()
        }
    }

    private lateinit var addressEnteredCallback: AddressEnteredCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = AddressLayoutBinding.inflate(inflater)

        binding.button.setOnClickListener { submitClicked() }
        return binding.root
    }

    private fun submitClicked() {
        addressEnteredCallback.addressEntered()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        require (context is AddressEnteredCallback) {
            "$context must implement ${AddressEnteredCallback::class.java}"
        }
        addressEnteredCallback = context
    }
}