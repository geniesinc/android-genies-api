package com.genies.avatar_api_sampleapp.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.genies.avatar_api_sampleapp.R
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.util.ToolbarBackAction
import com.genies.avatar_api_sampleapp.util.ToolbarHost
import com.genies.avatar_api_sampleapp.util.getBackgroundGradient
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.tv_signup_error

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarHost).updateToolbar(getString(R.string.title_register), ToolbarBackAction())

        btn_sign_up_next.setOnClickListener {
            showError("")
            val username = text_input_user.editText?.text.toString()
            val password = text_input_password.editText?.text.toString()
            viewModel.signUp(username, password)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeSignUpStatus()
    }

    private fun observeSignUpStatus() {
        viewModel.signupStatus.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is Success -> {
                        findNavController().navigate(
                            SignUpFragmentDirections.signupToSignupConfirmation(
                                text_input_user.editText?.text.toString()
                            )
                        )
                    }
                    is Error -> {
                        it.message?.let { message -> showError(message) }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        tv_signup_error.text = message
    }
}