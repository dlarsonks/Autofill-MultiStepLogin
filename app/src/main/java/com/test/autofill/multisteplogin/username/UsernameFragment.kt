package com.test.autofill.multisteplogin.username

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.R

/**
 * Created by dlarson on 10/27/17.
 */
class UsernameFragment :  Fragment() {
    companion object {
        val TAG = UsernameFragment::class.simpleName

        fun newInstance() : Fragment {
            return UsernameFragment()
        }
    }

    private var mainView: View? = null
    private lateinit var usernameEnteredCallback: UsernameEnteredCallback

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        container?.removeAllViews()

        mainView = inflater.inflate(R.layout.username_layout, container, false)

        if (mainView != null) {
            mainView?.findViewById<Button>(R.id.submitButton)?.setOnClickListener {
                submitClicked()
            }
        }

        return mainView
    }

    private fun submitClicked() {
        if (DEBUG) Log.d(TAG, "submitClicked: ")
        usernameEnteredCallback.usernameEntered()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is UsernameEnteredCallback) {
            usernameEnteredCallback = context
        }
    }
}

