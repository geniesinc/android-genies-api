package com.genies.avatar_api_library.model.assets

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComposerAsset(
    @Json(name = "assetId") val assetId: String,
    @Json(name = "available") val available: Boolean? = false,
    @Json(name = "lastModified") val lastModified: Long,
    @Json(name = "category") val category: String,
    @Json(name = "created") val created: Long,
    @Json(name = "name") val name: String,
    @Json(name = "gender") val gender: String
)