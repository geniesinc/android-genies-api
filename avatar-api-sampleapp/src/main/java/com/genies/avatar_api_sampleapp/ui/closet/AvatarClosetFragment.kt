package com.genies.avatar_api_sampleapp.ui.closet

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.genies.avatar_api_sampleapp.R
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.ui.assetslist.ComposerAssetsAdapter
import com.genies.avatar_api_sampleapp.util.ToolbarHost
import com.genies.avatar_api_sampleapp.util.ToolbarBackAction
import kotlinx.android.synthetic.main.fragment_user_closet.*

class AvatarClosetFragment : Fragment(R.layout.fragment_user_closet) {
    private val viewModel by viewModels<AvatarClosetViewModel>()
    private val assetsAdapter = ComposerAssetsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarHost).updateToolbar("Avatar closet", ToolbarBackAction())

        rv_closet_items.layoutManager = LinearLayoutManager(requireContext())
        rv_closet_items.adapter = assetsAdapter
        rv_closet_items.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )

        observeCloset()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCloset()
    }

    private fun observeCloset() {
        viewModel.closetLiveData.observe(viewLifecycleOwner) { closetResult ->
            when (closetResult) {
                is Success ->
                    assetsAdapter.setAssets(closetResult.data) { asset, action ->
                        when(action) {
                            ComposerAssetsAdapter.AssetAction.WITHDRAW ->
                                viewModel.withdrawAsset(asset)
                        }
                    }
                is Error ->
                    closetResult.message?.let { showText(it) }
            }
        }
        viewModel.assetActionLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showText(message)
            }
        }
    }

    private fun showText(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}