package com.genies.avatar_api_sampleapp.ui.assetslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.genies.avatar_api_sampleapp.R
import com.genies.avatar_api_sampleapp.model.AssetDisplayModel
import com.google.android.material.button.MaterialButton

class ComposerAssetsAdapter : RecyclerView.Adapter<ComposerAssetsAdapter.AssetViewHolder>() {
    enum class AssetAction { DEPOSIT, WITHDRAW }

    private var assetList: List<AssetDisplayModel> = listOf()
    private var onItemAction: (AssetDisplayModel, AssetAction) -> Unit = { _, _ -> }

    fun setAssets(
        assetList: List<AssetDisplayModel>,
        onItemAction: (AssetDisplayModel, AssetAction) -> Unit
    ) {
        this.assetList = assetList
        this.onItemAction = onItemAction
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        return AssetViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_asset, parent, false)
        )
    }

    inner class AssetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val assetNameTV: TextView = view.findViewById(R.id.tv_asset_name)
        private val assetIV: ImageView = view.findViewById(R.id.iv_asset_thumbnail)

        private val actionBtn: MaterialButton = view.findViewById(R.id.btn_asset_action)

        fun bind(asset: AssetDisplayModel) {
            assetNameTV.text = "${asset.composerAsset.name}"
            actionBtn.text =
                if (asset.closetItem != null && asset.closetItem.inPossession)
                    "WITHDRAW"
                else
                    "DEPOSIT"

            actionBtn.setOnClickListener {
                if (asset.closetItem != null && asset.closetItem.inPossession) {
                    onItemAction.invoke(
                        asset,
                        AssetAction.WITHDRAW
                    )
//                    actionBtn.text = "DEPOSIT"
                } else {
                    onItemAction.invoke(
                        asset,
                        AssetAction.DEPOSIT
                    )
//                    actionBtn.text = "WITHDRAW"

                }


            }
        }

    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        holder.bind(assetList[position])
    }

    override fun getItemCount(): Int = assetList.size
}