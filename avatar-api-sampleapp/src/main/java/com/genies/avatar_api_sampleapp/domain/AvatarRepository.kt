package com.genies.avatar_api_sampleapp.domain

import com.genies.avatar_api_library.api.AvatarAPIError
import com.genies.avatar_api_library.client.AvatarAPIClient
import com.genies.avatar_api_library.client.AvatarAPIClient.Companion.CATEGORY_HATS
import com.genies.avatar_api_library.model.assets.ClosetItem
import com.genies.avatar_api_library.model.assets.ComposerAsset
import com.genies.avatar_api_library.model.create_user.AvatarUser
import com.github.michaelbull.result.*

object AvatarRepository {
    private val userSessionRepository: UserSessionRepository = UserSessionRepository

    private val avatarApi: AvatarAPIClient =
        AvatarAPIClient("YOUR_API_KEY") // Get your API key: https://geniesinc.github.io/#step-one-apply-for-a-partner-account

    private var currentUser: AvatarUser? = null

    suspend fun getAllUsers(): Result<List<AvatarUser>, DomainError> {
        return getIdToken()
            .onFailure {
                return Err(DomainError("Invalid user session"))
            }
            .andThen { idToken ->
                avatarApi.getAllUsers(idToken).mapError { it.toDomainError() }
            }
    }

    suspend fun getUserByUsername(username: String): Result<AvatarUser, DomainError> {
        return getIdToken()
            .onFailure {
                return Err(DomainError("Invalid user session"))
            }
            .andThen { idToken ->
                avatarApi.getUser(idToken, username).mapError { it.toDomainError() }
            }
    }

    suspend fun getCurrentUser(): Result<AvatarUser, DomainError> {
        return if (currentUser != null) Ok(currentUser!!)
        else {
            getIdToken().andThen { idToken ->
                avatarApi.getCurrentUser(idToken)
                    .onSuccess {
                        currentUser = it
                    }.mapError { DomainError(it.reason) }
            }
        }
    }

    suspend fun getAssetsForCategory(): Result<List<ComposerAsset>, DomainError> {
        return getIdToken().andThen { idToken ->
            avatarApi.getAssetsForCategory(idToken, CATEGORY_HATS).mapError { it.toDomainError() }
        }
    }

    suspend fun getLatestAssets(): Result<List<ComposerAsset>, DomainError> {
        return getIdToken().andThen { idToken ->
            avatarApi.getLatestAssets(idToken).mapError { it.toDomainError() }
        }
    }

    suspend fun getClosetItems(): Result<List<ClosetItem>, DomainError> {
        return getCurrentUser()
            .andThen { user ->
                getIdToken().andThen { idToken ->
                    avatarApi.getClosetItems(idToken, user.userId)
                        .mapError { it.toDomainError() }
                }
            }

    }

    suspend fun depositClosetItem(assetId: String): Result<List<ClosetItem>, DomainError> {
        return getCurrentUser()
            .andThen { user ->
                getIdToken().andThen { idToken ->
                    avatarApi.depositAsset(idToken, user.userId, assetId)
                        .mapError { it.toDomainError() }
                }
            }
    }

    suspend fun withdrawClosetItem(
        assetId: String,
        assetInstanceId: String
    ): Result<List<ClosetItem>, DomainError> {
        return getCurrentUser()
            .andThen { user ->
                getIdToken()
                    .andThen { idToken ->
                        avatarApi.withdrawAsset(idToken, user.userId, assetId, assetInstanceId)
                            .mapError { it.toDomainError() }
                    }
            }
    }

    private suspend fun getIdToken(): Result<String, DomainError> {
        return userSessionRepository.getGeniesAuthSession().map { it.idToken.token }
    }

    fun clear() {
        currentUser = null
    }

    private fun AvatarAPIError.toDomainError() = DomainError(reason)


}