package com.genies.avatar_api_library.model.assets

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

enum class AssetActionOperation { deposit, withdraw }

@JsonClass(generateAdapter = true)
class AssetActionRequest(
    @Json(name = "op") val operation: AssetActionOperation,
    @Json(name = "item") val item: AssetActionItem
)

@JsonClass(generateAdapter = true)
data class AssetActionItem(
    @Json(name = "assetId") val assetId: String,
    @Json(name = "instanceId") val instanceId: String? = null
)