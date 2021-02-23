package com.genies.avatar_api_sampleapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.genies.avatar_api_sampleapp.R
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.util.ToolbarHost
import com.genies.avatar_api_sampleapp.util.getBackgroundGradient

import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.text_input_password
import kotlinx.android.synthetic.main.fragment_login.text_input_user


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarHost).updateToolbar(getString(R.string.title_login))

        btn_login.setOnClickListener {
            showError("")
            val username = text_input_user.editText?.text.toString()
            val password = text_input_password.editText?.text.toString()
            loginViewModel.logIn(username, password)
        }

        btn_sign_up.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.loginToSignup())
        }

        loginViewModel.checkLogin()
        observeLoginStatus()
    }

    private fun observeLoginStatus() {
        loginViewModel.loginState.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Success -> {
                        findNavController().navigate(LoginFragmentDirections.loginToLoggedIn())
                    }
                    is Error ->
                        showError(result.message)
                }
            }
        }
    }

    private fun showError(message: String) {
        tv_login_error.text = message
    }
}