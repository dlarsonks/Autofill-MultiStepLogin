package com.test.autofill.multisteplogin.password

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.test.autofill.BuildConfig.DEBUG
import com.test.autofill.R
import com.test.autofill.multisteplogin.StringUtils
import com.test.autofill.multisteplogin.notNull
import android.support.constraint.ConstraintSet

/**
 * Created by dlarson on 10/27/17.
 */
class PasswordFragment : Fragment() {
    val TAG = PasswordFragment::class.simpleName

    private lateinit var mainView: View
    private lateinit var passwordEnteredCallback: PasswordEnteredCallback
    private var numberOfPasswordFields: Int = 1

    private lateinit var constraintLayout: ConstraintLayout

    private lateinit var button: Button
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    companion object {

        private val number_of_password_fields = "number_of_password_fields_extra"
        fun newInstance(numberOfPasswordFields: Int) : Fragment {
            val bundle = Bundle()
            bundle.putInt(number_of_password_fields, numberOfPasswordFields)
            val passwordFragment = PasswordFragment()
            passwordFragment.arguments = bundle
            return passwordFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (DEBUG) Log.d(TAG, "onCreateView: ")

        container?.removeAllViews()

        if (DEBUG) Log.d(TAG, "onCreateView: numberOfPasswordFields = $numberOfPasswordFields")

        mainView = inflater.inflate(R.layout.password_layout, container, false)

        mainView.notNull {
            constraintLayout = mainView.findViewById(R.id.passwordLayout)
            button = mainView.findViewById(R.id.submitButton)
            passwordEditText = mainView.findViewById<EditText>(R.id.passwordEditText)
            confirmPasswordEditText = mainView.findViewById(R.id.confirmPasswordEditText)

            button.setOnClickListener {
                submitClicked()
            }

            when (numberOfPasswordFields) {
                1 -> {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)

                    constraintSet.connect(button.id, ConstraintSet.START, passwordEditText.id, ConstraintSet.START)
                    constraintSet.connect(button.id, ConstraintSet.END, passwordEditText.id, ConstraintSet.END)
                    constraintSet.connect(button.id, ConstraintSet.TOP, passwordEditText.id, ConstraintSet.BOTTOM)
                    constraintSet.applyTo(constraintLayout)

                    (confirmPasswordEditText.parent as ViewGroup).removeView(confirmPasswordEditText)
                }
                2 -> confirmPasswordEditText.visibility = View.VISIBLE
            }
        }

        return mainView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        numberOfPasswordFields = arguments?.getInt(number_of_password_fields, 1)?: 1
        if (DEBUG) Log.d(TAG, "onCreate: numberOfPasswordFields = $numberOfPasswordFields")
    }

    enum class PasswordValidationResult {
        PasswordsDoNotMatch, PasswordNotLongEnough, Success
    }

    private fun submitClicked() {
        if (DEBUG) Log.d(TAG, "submitClicked: ")

        when (validatePassword()) {
            PasswordValidationResult.Success -> passwordEnteredCallback.passwordEntered()
            PasswordValidationResult.PasswordNotLongEnough -> displayPasswordNotLongEnough()
            PasswordValidationResult.PasswordsDoNotMatch -> displayPasswordsDoNotMatch()
        }
    }

    private fun displayPasswordsDoNotMatch() {
        Toast.makeText(context, getString(R.string.passwords_do_not_match), Toast.LENGTH_LONG).show()
    }

    private fun displayPasswordNotLongEnough() {
        Toast.makeText(context, getString(R.string.password_min_length), Toast.LENGTH_LONG).show()
    }

    private fun validatePassword(): PasswordValidationResult {

        val password = passwordEditText.editableText.toString()
        val confirmationVisible = numberOfPasswordFields == 2

        var confirmPassword:String? = null
        if(confirmationVisible) {
            confirmPassword = confirmPasswordEditText.editableText.toString()
        }

        if(StringUtils.isBlank(password) || password.length < 3) {
            return PasswordValidationResult.PasswordNotLongEnough
        }
        if(confirmationVisible && !password.equals(confirmPassword)) {
            return PasswordValidationResult.PasswordsDoNotMatch
        }
        return PasswordValidationResult.Success
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is PasswordEnteredCallback) {
            passwordEnteredCallback = context
        }
    }
}

