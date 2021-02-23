package com.genies.avatar_api_library.model.assets

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvatarAsset(
    @Json(name = "assetId") val assetId: String,
    @Json(name = "collectionId") val collectionId: String,
    @Json(name = "originId") val originId: String,
    @Json(name = "created") val created: Long,
    @Json(name = "lastModified") val lastModified: Long,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "available") val available: Boolean,
    @Json(name = "rarity") val rarity: String,
    @Json(name = "maxInstanceCount") val maxInstanceCount: Int?,
    @Json(name = "category") val category: String,
    @Json(name = "tags") val tags: List<String>,
    @Json(name = "ordering") val ordering: Int,
    @Json(name = "location")  val location: String?
)