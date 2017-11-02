package com.test.autofill.multisteplogin.first_last_name

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.R
import com.test.autofill.multisteplogin.notNull

/**
 * Created by dlarson on 10/27/17.
 */
class FirstAndLastNameFragment :  Fragment() {
    val TAG = FirstAndLastNameFragment::class.simpleName

    private lateinit var mainView: View
    private lateinit var firstAndLastNameEnteredCallback: FirstAndLastNameEnteredCallback

    companion object {
        fun newInstance() : Fragment {
            return FirstAndLastNameFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        container?.removeAllViews()

        mainView = inflater.inflate(R.layout.first_last_name_layout, container, false)

        mainView.notNull {
            mainView.findViewById<Button>(R.id.submitButton)?.setOnClickListener {
                submitClicked()
            }
        }

        return mainView
    }

    private fun submitClicked() {
        if (DEBUG) Log.d(TAG, "submitClicked: ")
        firstAndLastNameEnteredCallback.firstAndLastNameEntered()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FirstAndLastNameEnteredCallback) {
            firstAndLastNameEnteredCallback = context
        }
    }
}