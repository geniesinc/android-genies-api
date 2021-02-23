package com.genies.avatar_api_sampleapp.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.genies.avatar_api_sampleapp.R
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.util.ToolbarBackAction
import com.genies.avatar_api_sampleapp.util.ToolbarHost
import com.genies.avatar_api_sampleapp.util.getBackgroundGradient
import kotlinx.android.synthetic.main.fragment_signup_confirm.*
import kotlinx.android.synthetic.main.fragment_signup_confirm.tv_signup_error

class SignUpConfirmationFragment : Fragment(R.layout.fragment_signup_confirm) {

    private val args: SignUpConfirmationFragmentArgs by navArgs()
    private val viewModel by viewModels<SignUpConfirmationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarHost).updateToolbar("Confirmation", ToolbarBackAction())

        btn_confirm_sign_up_next.setOnClickListener {
            showError("")
            val code = text_confirmation.editText?.text.toString()
            viewModel.confirmSignUp(args.username, code)
        }

        observeConfirmSignupStatus()
    }

    private fun observeConfirmSignupStatus() {
        viewModel.signupConfirmationStatus.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is Success -> {
                        findNavController().popBackStack(R.id.loginFragment, false)
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