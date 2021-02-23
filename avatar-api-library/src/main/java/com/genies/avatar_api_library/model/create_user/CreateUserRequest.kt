package com.genies.avatar_api_library.model.create_user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateUserRequest(@Json(name = "username") val username: String)