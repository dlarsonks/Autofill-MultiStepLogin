package com.test.autofill.multisteplogin.paymentcard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.autofill.databinding.PaymentCardLayoutBinding

/**
 * Created by dlarson at 1/21/21
 */
class PaymentCardFragment : Fragment() {
    companion object {
        val TAG: String = PaymentCardFragment::class.java.simpleName
        fun newInstance(): PaymentCardFragment {
            return PaymentCardFragment()
        }
    }

    private lateinit var paymentCardEnteredCallback: PaymentCardEnteredCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = PaymentCardLayoutBinding.inflate(inflater)

        binding.button.setOnClickListener { submitClicked() }
        return binding.root
    }

    private fun submitClicked() {
        paymentCardEnteredCallback.paymentCardEntered()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        require (context is PaymentCardEnteredCallback) {
            "$context must implement ${PaymentCardEnteredCallback::class.java}"
        }
        paymentCardEnteredCallback = context
    }
}