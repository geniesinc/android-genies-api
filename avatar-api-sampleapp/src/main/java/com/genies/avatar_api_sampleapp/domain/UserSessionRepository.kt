package com.genies.avatar_api_sampleapp.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.genies.avatar_api_sampleapp.AvatarApiSampleApplication
import com.genies.genies_cognito_signin.client.AuthSession
import com.genies.genies_cognito_signin.client.AvatarAuthClient

import com.github.michaelbull.result.*


/**
 *
 */
object UserSessionRepository {

    private val clientId = "YOUR_CLIENT_ID" // To get your client id apply for a partner account: https://geniesinc.github.io/#step-one-apply-for-a-partner-account
    private val clientSecret = null

    private val authClient: AvatarAuthClient = AvatarAuthClient(
        AvatarApiSampleApplication.applicationContext(),
        clientId = clientId,
        clientSecret = clientSecret
    )

    private var authSession: AuthSession? = null

    enum class SessionState {
        LOADING,
        LOGGED_IN,
        LOGGED_OUT
    }

    private val _sessionState: MutableLiveData<SessionState> by lazy {
        MutableLiveData<SessionState>(
            SessionState.LOADING
        )
    }

    val sessionState: LiveData<SessionState> = _sessionState

    suspend fun getGeniesAuthSession(): Result<AuthSession, DomainError> {
        return authClient.getSession()
            .onSuccess { authSession ->
                this.authSession = authSession
                _sessionState.value = SessionState.LOGGED_IN
            }
            .onFailure {
                this.authSession = null
                _sessionState.value = SessionState.LOGGED_OUT
            }
            .mapError { DomainError(it.reason) } // Transform error to domain Error
    }


    suspend fun signIn(username: String, password: String): Result<DomainSuccess, DomainError> {
        return authClient.signIn(username, password)
            .onSuccess { authSession ->
                this.authSession = authSession
                _sessionState.value = SessionState.LOGGED_IN
            }
            .map { DomainSuccess }
            .mapError { DomainError(it.reason) }
    }

    suspend fun signUp(username: String, password: String): Result<DomainSuccess, DomainError> {
        return authClient.signUp(username, password)
            .map { DomainSuccess } // Transform success to domain Success
            .mapError { DomainError(it.reason) } // Transform error to domain Error
    }

    suspend fun confirmSignUp(
        username: String,
        confirmationCode: String
    ): Result<DomainSuccess, DomainError> {
        return authClient.confirmSignUp(username, confirmationCode)
            .map { DomainSuccess }
            .mapError { DomainError(it.reason) }
    }


    suspend fun forgotPassword(username: String): Result<DomainSuccess, DomainError> {
        return authClient.forgotPassword(username)
            .map { DomainSuccess }
            .mapError { DomainError(it.reason) }
    }

    suspend fun confirmForgotPassword(
        username: String,
        newPassword: String,
        confirmationCode: String
    ): Result<DomainSuccess, DomainError> {
        return authClient.confirmForgotPassword(username, newPassword, confirmationCode)
            .map { DomainSuccess }
            .mapError { DomainError(it.reason) }
    }

    fun signOut() {
        authClient.signOut()
        _sessionState.value = SessionState.LOGGED_OUT
        authSession = null
    }
}

/**
 * Class representing a domain error with an attached message
 * @property message The error message
 */
data class DomainError(val message: String)

/**
 * Class representing a domain operation success
 */
object DomainSuccess