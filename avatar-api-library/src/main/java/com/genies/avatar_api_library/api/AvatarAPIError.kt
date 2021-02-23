package com.genies.avatar_api_library.api

sealed class AvatarAPIError(val reason: String)
class ServerError(reason: String) : AvatarAPIError(reason)
class NetworkError(reason: String) : AvatarAPIError(reason)
object UnknownError : AvatarAPIError("Unknown error")