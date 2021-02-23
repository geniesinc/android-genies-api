package com.genies.avatar_api_sampleapp.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.genies.avatar_api_sampleapp.R
import com.genies.avatar_api_sampleapp.util.ToolbarHost
import com.genies.avatar_api_sampleapp.util.ToolbarNoAction

import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val viewModel: WelcomeFragmentViewModel by viewModels<WelcomeFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as ToolbarHost).updateToolbar("Welcome to Genies", ToolbarNoAction)

        btn_login.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.welcomeToLogin())
        }

        observeSignedInStatus()
    }

    private fun observeSignedInStatus() {
        viewModel.signInState.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { findNavController().navigate(
                WelcomeFragmentDirections.welcomeToLoggedIn()) }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkSessionStatus()
    }
}