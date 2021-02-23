package com.genies.avatar_api_library.api

import com.genies.avatar_api_library.*
import com.genies.avatar_api_library.BuildConfig.API_HOST
import com.genies.avatar_api_library.model.EmptyBody
import com.genies.avatar_api_library.model.EmptyResponse
import com.genies.avatar_api_library.model.ErrorResponse
import com.genies.avatar_api_library.model.assets.*
import com.genies.avatar_api_library.model.assets.ClosetItem
import com.genies.avatar_api_library.model.create_user.CreateUserRequest
import com.genies.avatar_api_library.model.create_user.AvatarUser
import com.github.michaelbull.result.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.HttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


typealias ApiResponse<T> = Result<T, AvatarAPIError>

internal class AvatarAPI internal constructor(
    private val apiKey: String,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) {

    constructor(apiKey: String) : this(
        apiKey,
        DefaultDispatcherProvider()
    )

    companion object {
        private const val apiHost: String = BuildConfig.API_HOST
        private const val stage = BuildConfig.STAGE
    }

    private var moshi = provideMoshi()
    private val okHttpClient: OkHttpClient = provideOkHttp()

    internal enum class HttpMethod { GET, POST, PATCH, DELETE }

    private fun getBaseUrlBuilder(): HttpUrl.Builder =
        HttpUrl.Builder().scheme("https").host(apiHost).addPathSegment(stage)

    suspend fun createUser(idToken: String, request: CreateUserRequest): ApiResponse<AvatarUser> {
        val url = getBaseUrlBuilder().addPathSegment("user").build()
        return request(
            idToken = idToken,
            method = HttpMethod.POST,
            url = url,
            requestBody = request,
            responseAdapter = moshi.adapter(AvatarUser::class.java)
        )
    }

    suspend fun deleteUser(idToken: String, userId: String): ApiResponse<OperationSuccess> {
        val url = getBaseUrlBuilder().addPathSegment("user").addPathSegment(userId).build()
        return requestEmptyResponse(
            idToken = idToken,
            method = HttpMethod.DELETE,
            url = url,
            requestBody = EmptyBody
        ).map { OperationSuccess() }
    }

    suspend fun getUser(idToken: String, username: String): ApiResponse<AvatarUser> {
        val url = getBaseUrlBuilder().addPathSegment("user")
            .addEncodedQueryParameter("username", username).build()
        val listType = Types.newParameterizedType(List::class.java, AvatarUser::class.java)
        val response: Result<List<AvatarUser>, AvatarAPIError> =
            request(
                idToken = idToken,
                method = HttpMethod.GET,
                url = url,
                requestBody = EmptyBody,
                responseAdapter = moshi.adapter(listType)
            )
        return response.map { it[0] }
    }

    suspend fun getAllUsers(idToken: String): ApiResponse<List<AvatarUser>> {
        val url = getBaseUrlBuilder().addPathSegment("user").build()
        val listType = Types.newParameterizedType(List::class.java, AvatarUser::class.java)
        val response: Result<List<AvatarUser>, AvatarAPIError> =
            request(
                idToken = idToken,
                method = HttpMethod.GET,
                url = url,
                requestBody = EmptyBody,
                responseAdapter = moshi.adapter(listType)
            )
        return response
    }

    suspend fun getCurrentUser(idToken: String): Result<AvatarUser, AvatarAPIError> {
        val url = getBaseUrlBuilder().addPathSegment("user").build()
        val listType = Types.newParameterizedType(List::class.java, AvatarUser::class.java)
        val response: Result<List<AvatarUser>, AvatarAPIError> =
            request(
                idToken = idToken,
                method = HttpMethod.GET,
                url = url,
                requestBody = EmptyBody,
                responseAdapter = moshi.adapter(listType)
            )
        return response.map { it[0] }
    }

    suspend fun getLatestAssets(idToken: String): ApiResponse<List<ComposerAsset>> {
        val url = getBaseUrlBuilder()
            .addPathSegment("asset")
            .addQueryParameter("modifiedSince", "1")
            .build()
        val listType = Types.newParameterizedType(List::class.java, ComposerAsset::class.java)
        return request(
            idToken = idToken,
            method = HttpMethod.GET,
            url = url,
            requestBody = EmptyBody,
            responseAdapter = moshi.adapter(listType)
        )
    }

    suspend fun getCollection(
        idToken: String,
        collectionId: String
    ): ApiResponse<List<AvatarAsset>> {
        val url = getBaseUrlBuilder()
            .addPathSegment("collection")
            .addPathSegment(collectionId)
            .build()
        val listType = Types.newParameterizedType(List::class.java, AvatarAsset::class.java)
        return request(
            idToken = idToken,
            method = HttpMethod.GET,
            url = url,
            requestBody = EmptyBody,
            responseAdapter = moshi.adapter(listType)
        )
    }

    suspend fun getAssetListForCategory(
        idToken: String,
        category: String
    ): ApiResponse<List<ComposerAsset>> {
        val url = getBaseUrlBuilder()
            .addPathSegment("asset")
            .addQueryParameter("category", category)
            .build()
        val listType = Types.newParameterizedType(List::class.java, ComposerAsset::class.java)
        return request(
            idToken = idToken,
            method = HttpMethod.GET,
            url = url,
            requestBody = EmptyBody,
            responseAdapter = moshi.adapter(listType)
        )
    }

    suspend fun getClosetItems(
        idToken: String,
        userId: String
    ): ApiResponse<List<ClosetItem>> {
        val url = getBaseUrlBuilder()
            .addPathSegment("user")
            .addPathSegment(userId)
            .addPathSegment("closet").build()
        val listType = Types.newParameterizedType(List::class.java, ClosetItem::class.java)
        return request(
            idToken = idToken,
            method = HttpMethod.GET,
            url = url,
            requestBody = EmptyBody,
            responseAdapter = moshi.adapter(listType)
        )
    }

    suspend fun depositAsset(
        idToken: String,
        userId: String,
        assetId: String
    ): ApiResponse<List<ClosetItem>> {
        val url = getBaseUrlBuilder()
            .addPathSegment("user")
            .addPathSegment(userId)
            .addPathSegment("closet").build()
        val listType = Types.newParameterizedType(List::class.java, ClosetItem::class.java)
        val body = listOf(
            AssetActionRequest(
                operation = AssetActionOperation.deposit,
                item = AssetActionItem(assetId)
            )
        )
        return request(
            idToken = idToken,
            method = HttpMethod.PATCH,
            url = url,
            requestBody = body,
            responseAdapter = moshi.adapter(listType)
        )
    }

    suspend fun withdrawAsset(
        idToken: String,
        userId: String,
        assetId: String,
        assetInstanceId: String
    ): ApiResponse<List<ClosetItem>> {
        val url = getBaseUrlBuilder()
            .addPathSegment("user")
            .addPathSegment(userId)
            .addPathSegment("closet").build()
        val listType = Types.newParameterizedType(List::class.java, ClosetItem::class.java)
        val body = listOf(
            AssetActionRequest(
                operation = AssetActionOperation.withdraw,
                item = AssetActionItem(assetId = assetId, instanceId = assetInstanceId)
            )
        )
        return request(
            idToken = idToken,
            method = HttpMethod.PATCH,
            url = url,
            requestBody = body,
            responseAdapter = moshi.adapter(listType)
        )
    }

    private suspend inline fun <reified ReqBody : Any, reified Resp : Any> request(
        idToken: String,
        method: HttpMethod,
        url: HttpUrl,
        requestBody: ReqBody,
        responseAdapter: JsonAdapter<Resp>
    ): Result<Resp, AvatarAPIError> = withContext(
        dispatchers.io()
    ) {
        suspendCoroutine<Result<Resp, AvatarAPIError>> { continuation ->
            runCatching {
                // Get request body if present
                val reqBody: RequestBody? =
                    if (requestBody !is EmptyBody) moshi.adapter(ReqBody::class.java).toJson(requestBody)
                        .toRequestBody() else null

                // Build request
                val request = Request.Builder()
                    .url(url)
                    .addAuthHeader(idToken)
                    .method(method.toString(), reqBody)
                    .build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resume(
                            Err(
                                NetworkError(
                                    e.message ?: "Unknown network exception"
                                )
                            )
                        )
                    }

                    override fun onResponse(call: Call, response: Response) {
                        continuation.resume(parseResponse(response, responseAdapter))
                    }

                })


            }.onFailure {
                it.printStackTrace()
                continuation.resume(Err(UnknownError))
            }
        }
    }

    private suspend inline fun <reified ReqBody : Any> requestEmptyResponse(
        idToken: String,
        method: HttpMethod,
        url: HttpUrl,
        requestBody: ReqBody? = null
    ): Result<EmptyResponse, AvatarAPIError> = withContext(
        dispatchers.io()
    ) {
        suspendCoroutine< com.github.michaelbull.result.Result<EmptyResponse, AvatarAPIError>> { continuation ->
            runCatching {

                // Get request body if present
                val reqBody: RequestBody? =
                    if (requestBody != null) moshi.adapter(ReqBody::class.java).toJson(requestBody)
                        .toRequestBody() else null

                // Build request
                val request = Request.Builder()
                    .url(url)
                    .addAuthHeader(idToken)
                    .method(method.toString(), reqBody).build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resume(
                            Err(
                                NetworkError(
                                    e.message ?: "Unknown network exception"
                                )
                            )
                        )
                    }

                    override fun onResponse(call: Call, response: Response) {
                        continuation.resume(parseEmptyResponse(response))
                    }

                })


            }.onFailure {
                it.printStackTrace()
                continuation.resume(Err(UnknownError))
            }
        }
    }

    private inline fun <reified Resp : Any> parseResponse(
        response: Response,
        responseAdapter: JsonAdapter<Resp>
    ): Result<Resp, AvatarAPIError> {
        // Get response adapter
        if (response.isSuccessful) {
            val respObj = try {
                responseAdapter.fromJson(response.body!!.source())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            return if (respObj != null)
                Ok(respObj)
            else Err(UnknownError)

        } else {
            val moshiErrorAdapter = moshi.adapter(ErrorResponse::class.java)
            val errorObj = try {
                moshiErrorAdapter.fromJson(response.body!!.source())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            return if (errorObj != null)
                Err(ServerError(errorObj.message))
            else
                Err(UnknownError)
        }
    }

    private inline fun parseEmptyResponse(response: Response): Result<EmptyResponse, AvatarAPIError> {
        if (response.isSuccessful) {
            return Ok(EmptyResponse)
        } else {
            val moshiErrorAdapter = moshi.adapter(ErrorResponse::class.java)
            val errorObj = try {
                moshiErrorAdapter.fromJson(response.body!!.source())
            } catch (e: Exception) {
                null
            }
            return if (errorObj != null)
                Err(ServerError(errorObj.message))
            else
                Err(UnknownError)
        }
    }

    private fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private fun provideOkHttp(): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            client.addInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        client.addInterceptor(AuthInterceptor(apiKey))
        return client.build()
    }

    private fun Request.Builder.addAuthHeader(idToken: String): Request.Builder {
        return addHeader("Authorization", "Bearer $idToken").addHeader("x-api-key", apiKey)
    }



    internal class AuthInterceptor(private val apiKey: String) :
        Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request =
                chain.request().newBuilder().addHeader("x-api-key", apiKey).build()
            return chain.proceed(request)
        }
    }
}

class OperationSuccess

