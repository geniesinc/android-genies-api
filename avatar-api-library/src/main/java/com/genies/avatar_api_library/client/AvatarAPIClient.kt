package com.genies.avatar_api_library.client

import com.genies.avatar_api_library.api.AvatarAPI
import com.genies.avatar_api_library.api.AvatarAPIError
import com.genies.avatar_api_library.api.OperationSuccess
import com.genies.avatar_api_library.model.assets.ClosetItem
import com.genies.avatar_api_library.model.create_user.AvatarUser
import com.genies.avatar_api_library.model.create_user.CreateUserRequest
import com.github.michaelbull.result.Result

/**
 * Local client for working with the Genies Avatar API
 *
 * Manages creating and deleting users and getting avatar assets lists
 * @param apiKey Required: client api key
 */
class AvatarAPIClient(apiKey: String) {

    private var avatarAPI: AvatarAPI = AvatarAPI(apiKey = apiKey)

    companion object {
        const val CATEGORY_HATS = "hats"
        const val CATEGORY_SHIRTS = "shirts"
        const val CATEGORY_HOODIE = "hoodie"
    }

    /**
     * Creates a user
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param username Required: username for the user to be created
     * @return Result of the create user operation
     */
    suspend fun createUser(idToken: String, username: String): Result<AvatarUser, AvatarAPIError> {
        val request = CreateUserRequest(username)
        return avatarAPI.createUser(idToken, request)
    }

    /**
     * Deletes a user
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param userId Required: user id for the user to be created
     * @return Result of the delete user operation
     */
    suspend fun deleteUser(
        idToken: String,
        userId: String
    ): Result<OperationSuccess, AvatarAPIError> {
        return avatarAPI.deleteUser(idToken, userId)
    }

    /**
     * Gets the user details associated to a username
     *
     * @param idToken Required: idToken for the currently authenticated user
     * @return User details
     */
    suspend fun getCurrentUser(idToken: String): Result<AvatarUser, AvatarAPIError> =
        avatarAPI.getCurrentUser(idToken)

    /**
     * Gets the user details associated to a username
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param username Required: username
     * @return User details
     */
    suspend fun getUser(idToken: String, username: String): Result<AvatarUser, AvatarAPIError> =
        avatarAPI.getUser(idToken, username)

    /**
     * Gets a list of user details
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @return Users details
     */
    suspend fun getAllUsers(idToken: String): Result<List<AvatarUser>, AvatarAPIError> =
        avatarAPI.getAllUsers(idToken)

    /**
     * Gets the avatar assets list
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @return List of Avatar assets
     */
    suspend fun getLatestAssets(idToken: String) = avatarAPI.getLatestAssets(idToken)

    /**
     *  Gets the avatar assets associated with a collection
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param collectionId Required: Avatar collection id
     * @return List of Avatar assets in the collection
     */
    suspend fun getCollection(idToken: String, collectionId: String) =
        avatarAPI.getCollection(idToken, collectionId)

    /**
     * Internal method to map a [AvatarAPIError] to [AvatarClientError]
     *
     * @return
     */
    private fun AvatarAPIError.mapToClientError(): AvatarClientError {
        return AvatarClientError(message = reason)
    }

    /**
     * Gets a list of asset for the specified category
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param category Required: Avatar asset category name
     */
    suspend fun getAssetsForCategory(idToken: String, category: String) =
        avatarAPI.getAssetListForCategory(idToken, category)

    /**
     * Gets a list of the items in the user's closet
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param userId Required: the user id
     * @return List of [ClosetItem] in the user's avatar closet
     */
    suspend fun getClosetItems(idToken: String, userId: String): Result<List<ClosetItem>, AvatarAPIError> =
        avatarAPI.getClosetItems(idToken, userId)

    /**
     * Deposits an item to the user's closet
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param userId Required: the user id
     * @param assetId Required: The id of the asset to be deposited to the user's closet
     * @return List<[ClosetItem]> if the operation is successful, [AvatarAPIError] otherwise
     */
    suspend fun depositAsset(
        idToken: String,
        userId: String,
        assetId: String
    ): Result<List<ClosetItem>, AvatarAPIError> =
        avatarAPI.depositAsset(idToken, userId, assetId)

    /**
     * Deposits an item to the user's closet
     *
     * @param idToken Required: idToken for the currently authenticated app session
     * @param userId Required: the user id
     * @param assetId Required: The id of the asset to be withdrawn from the user's closet
     * @param instanceId Required: the instance id of the asset to be withdrawn from the user's closet
     * @return List<[ClosetItem]> if the operation is successful, [AvatarAPIError] otherwise
     */
    suspend fun withdrawAsset(
        idToken: String,
        userId: String,
        assetId: String,
        instanceId: String
    ): Result<List<ClosetItem>, AvatarAPIError> =
        avatarAPI.withdrawAsset(idToken, userId, assetId, instanceId)

}