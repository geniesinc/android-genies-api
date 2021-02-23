package com.genies.avatar_api_sampleapp.model

import com.genies.avatar_api_library.model.assets.ClosetItem
import com.genies.avatar_api_library.model.assets.ComposerAsset

/**
 * Data class containing a composer asset and a flag showing if the item is in the user's closet or not
 */
data class AssetDisplayModel(val composerAsset: ComposerAsset, val closetItem: ClosetItem? = null)