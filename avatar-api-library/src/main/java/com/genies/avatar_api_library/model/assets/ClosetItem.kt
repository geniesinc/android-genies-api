package com.genies.avatar_api_library.model.assets

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClosetItem(
    @Json(name = "assetId") val assetId: String,
    @Json(name = "instanceId") val instanceId: String,
    @Json(name = "inPossession") val inPossession: Boolean,
    @Json(name = "updated") val updated: Long
)
