package com.genies.avatar_api_sampleapp.ui.assetslist

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.genies.avatar_api_sampleapp.R
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.util.ToolbarHost
import com.genies.avatar_api_sampleapp.util.ToolbarNoAction

import kotlinx.android.synthetic.main.fragment_assets_list.*

class AvatarAssetsListFragment : Fragment(R.layout.fragment_assets_list) {

    private val viewModel by viewModels<AvatarAssetsListViewModel>()
    private val assetsAdapter = ComposerAssetsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarHost).updateToolbar("Asset library", ToolbarNoAction)

        btn_sign_out.setOnClickListener {
            viewModel.signOut()
        }
        btn_closet.setOnClickListener {
            findNavController().navigate(AvatarAssetsListFragmentDirections.assetsListToCloset())
        }

        rv_avatar_items.layoutManager = LinearLayoutManager(requireContext())
        rv_avatar_items.adapter = assetsAdapter
        rv_avatar_items.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )

        observeSignOutStatus()
        observeAssets()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAssets()
    }

    private fun observeAssets() {
        viewModel.avatarAssetsLiveData.observe(viewLifecycleOwner) { assetsResult ->
            when (assetsResult) {
                is Success ->
                    assetsAdapter.setAssets(assetsResult.data) { asset, action ->
                        when (action) {
                            ComposerAssetsAdapter.AssetAction.DEPOSIT ->
                                viewModel.depositAsset(asset)
                            ComposerAssetsAdapter.AssetAction.WITHDRAW ->
                                viewModel.withdrawAsset(asset)
                        }
                    }
                is Error ->
                    assetsResult.message?.let { showText(it) }
            }
        }
        viewModel.assetActionLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showText(message)
            }
        }
    }

    private fun observeSignOutStatus() {
        viewModel.signOutState.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()
                ?.let { findNavController().navigate(AvatarAssetsListFragmentDirections.assetsListToWelcomeScreen()) }
        }
    }

    private fun showText(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}