package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.salesforce.gamification.api.GameAuthenticator
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import okhttp3.HttpUrl
import retrofit2.HttpException
import java.net.HttpURLConnection

/**
 * ForceAuthManager class handles authentication of Salesforce credentials
 */
class ForceAuthManager(val mContext: Context): ForceAuthenticator, GameAuthenticator {

    enum class AuthenticationStatus {
        AUTHENTICATED,
        UNAUTHENTICATED
    }

    private val authenticationStatus = MutableLiveData<AuthenticationStatus>()
    val authenticationStatusLiveData: LiveData<AuthenticationStatus> = authenticationStatus
    companion object {
        private const val TAG = "ForceAuthManager"
        private const val AUTH_SCOPE = "api refresh_token"
        private const val RESPONSE_TYPE_VALUE = "code_credentials"
    }

    var auth: ForceAuth? = null
    override fun getAccessToken(): String? {
        val forceAuth = getForceAuth()
        forceAuth?.let {
            return it.accessToken
        }
        return null
    }

    override suspend fun grantAccessToken(): String? {
        Logger.d(TAG, "grantAccessToken()")
        val connectedApp = getConnectedApp()
        if (auth != null) {
            auth?.refreshToken?.let {
                // Refresh access token

                val refreshAuth = refreshAccessToken(
                    url = ForceConfig.getRefreshAccessTokenRequestUrl(connectedApp.communityUrl),
                    consumerKey = connectedApp.consumerKey,
                    consumerSecret = connectedApp.consumerSecret,
                    it
                )
                if (refreshAuth != null) {
                    return refreshAuth.accessToken
                }
                return null
            }
        } else {
            val savedAuth = retrieveAuth()
            auth = savedAuth
            val savedRefreshToken = savedAuth?.refreshToken
            savedRefreshToken?.let {
                val refreshAuth = refreshAccessToken(
                    url = ForceConfig.getRefreshAccessTokenRequestUrl(connectedApp.communityUrl),
                    consumerKey = connectedApp.consumerKey,
                    consumerSecret = connectedApp.consumerSecret,
                    it
                )
                if (refreshAuth != null) {
                    return refreshAuth.accessToken
                }
                return null
            }
        }
        return null
    }

    /**
     * Authenticate Community Login by generating access token
     * OAuth 2.0 Authorization Code and Credentials Flow
     * https://help.salesforce.com/s/articleView?id=sf.remoteaccess_authorization_code_credentials_flow.htm&type=5
     */
    suspend fun authenticate(
        communityUrl: String,
        consumerKey: String,
        callbackUrl: String,
        userName: String,
        password: String
    ): String? {

        try {
            requestAuthorizationCode(
                communityUrl = communityUrl,
                consumerKey = consumerKey,
                callbackUrl = callbackUrl,
                userName = userName,
                password = password
            )
        } catch (ex: HttpException) {
            Logger.e(TAG, "Access token HttpException ${ex.message}", ex)
            val responseCode = ex.response()?.code()
            if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val redirectUrl = ex.response()?.raw()?.request?.url
                redirectUrl?.let {
                    val code = getAuthorizationCode(it)
                    Logger.d(TAG, "Auth code : $code")
                    code?.let { authCode ->
                        return requestAccessToken(
                            communityUrl = communityUrl,
                            authCode,
                            consumerKey = consumerKey,
                            callbackUrl = callbackUrl
                        )
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
            Logger.e(TAG, "Failed to generate access token ${ex.message}", ex)
        }
        return null
    }

    /**
     * Get authorization code from the redirect URL.
     * Replace %3D to '=' in the url query parameter value.
     */
    private fun getAuthorizationCode(url: HttpUrl): String? {
        val queryItems = url.queryParameter("code")
        return queryItems?.replace("%3D", "=")
    }

    /**
     * Part 1 - Makes a Headless Request for an Authorization Code
     */
    private suspend fun requestAuthorizationCode(
        communityUrl: String,
        consumerKey: String,
        callbackUrl: String,
        userName: String,
        password: String
    ) {

        ForceClient.authApi.requestAuthorizationCode(
            ForceConfig.getAuthorizationCodeRequestUrl(communityUrl),
            ForceConfig.HEADER_AUTH_REQUEST_TYPE_VALUE,
            AUTH_SCOPE,
            RESPONSE_TYPE_VALUE,
            consumerKey,
            callbackUrl,
            userName,
            password
        )

    }

    /**
     * Part 2 - Requests an Access Token (and Refresh Token)
     */
    private suspend fun requestAccessToken(
        communityUrl: String,
        authCode: String,
        consumerKey: String,
        callbackUrl: String
    ): String? {

        val response = ForceClient.authApi.requestAccessToken(
            ForceConfig.getAccessTokenRequestUrl(communityUrl),
            authCode,
            ForceConfig.AUTHORIZATION_GRANT_TYPE,
            consumerKey,
            callbackUrl
        )
        response.onSuccess {
            Logger.d(TAG, "Access token success: ${it}")
            saveAuth(it)
            return it.accessToken
        }.onFailure {
            Logger.d(TAG, "Access token failure : ${it.message}")
        }
        return null
    }

    /**
     * Refresh access token
     * OAuth 2.0 Refresh Token Flow - use refresh token to get a new accessToken
     * https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_refresh_token_flow.htm&type=5
     */
    private suspend fun refreshAccessToken(
        url: String,
        consumerKey: String,
        consumerSecret: String?,
        refreshToken: String
    ): ForceAuth? {
        refreshToken?.let {
            val response =
                ForceClient.authApi.refreshAccessToken(
                    url,
                    ForceConfig.REFRESH_GRANT_TYPE,
                    consumerKey,
                    it,
                    consumerSecret
                )
            response.onSuccess {newAuth ->
                val newAuthToSave = ForceAuth(
                    accessToken = newAuth.accessToken,
                    instanceURL = newAuth.instanceURL,
                    identityURL = newAuth.identityURL,
                    tokenType = newAuth.tokenType,
                    timestamp = newAuth.timestamp,
                    signature = newAuth.signature,
                    refreshToken = refreshToken
                )
                saveAuth(newAuthToSave)
                return newAuthToSave
            }.onFailure {
                Logger.d(TAG,"refresh token returned failure: ${it.message}  Localized message: ${it.localizedMessage}")
                authenticationStatus.postValue(AuthenticationStatus.UNAUTHENTICATED)
                return null
            }
        }
        return null
    }

    private fun saveAuth(auth: ForceAuth) {
        this.auth = auth
        ForceAuthEncryptedPreference.saveAuth(mContext, auth)
        PrefHelper.customPrefs(mContext).set(AppConstants.KEY_USER_IDENTIFIER, auth.identityURL)
    }

    fun retrieveAuth(): ForceAuth? {
        val userIdentifier =
            PrefHelper.customPrefs(mContext).get<String>(AppConstants.KEY_USER_IDENTIFIER, null)
        userIdentifier?.let {
            return ForceAuthEncryptedPreference.getAuth(mContext, it)
        }
        return null
    }

    fun getForceAuth(): ForceAuth? {
        auth?.let {
            return it
        }
        val savedAuth = retrieveAuth()
        savedAuth?.let {
            auth = it
            return it
        }

        Logger.d(TAG, "No auth found. Please Log in.")
        return null
    }

    fun getInstanceUrl(): String? {
        return Common.getInstanceUrl(mContext)
    }

    fun getLoyaltyProgramName(): String? {
        return Common.getLoyaltyProgramName(mContext)
    }

    fun getConnectedApp(): ConnectedApp {
        val selectedConnectedApp = PrefHelper.instancePrefs(mContext)
            .get<String>(AppConstants.KEY_SELECTED_INSTANCE_URL, null)
        val connectedApp = selectedConnectedApp?.let { selectedConnectedApp ->
            ForceConnectedAppEncryptedPreference.getConnectedApp(
                mContext,
                selectedConnectedApp
            )
        } ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP
        return connectedApp
    }

    suspend fun revokeAccessToken() {
        val accessToken = getForceAuth()?.accessToken
        accessToken?.let {
            val connectedApp = getConnectedApp()
            val url = ForceConfig.getRevokeAccessTokenRequestUrl(connectedApp.communityUrl)
            val result = ForceClient.authApi.revokeAccessToken(url, accessToken = it)

            result.onSuccess {
                Logger.d(TAG, "Revoked Access token successfully!")
            }.onFailure {
                Logger.d(TAG, "Could not revoke Access token! ${it.message}")
            }
            auth = null
        }
    }

    suspend fun getFirstNameLastNameFromContactEmail(email: String): ContactRecord? {
        val accessToken = getForceAuth()?.accessToken
        accessToken?.let {
            val result = ForceClient.authApi.getFirstNameLastNameFromContactEmail(
                getContactDetailsSOQLUrl(),
                getContactDetailsSOQLQuery(email),
                "Bearer $accessToken"
            )
            result.onSuccess { queryResult: QueryResult<ContactRecord> ->
                queryResult?.records?.let {
                    Logger.d(TAG, "getFirstNameLastNameFromContactEmail records: ${it}")
                    if (!it.isNullOrEmpty()) {
                        return it.get(0)
                    }
                }
            }
        }
        return null
    }
    private fun getContactDetailsSOQLUrl(): String {
        return getInstanceUrl() + ForceConfig.SOQL_QUERY_PATH + ForceConfig.SOQL_QUERY_VERSION + ForceConfig.QUERY
    }

    private fun getContactDetailsSOQLQuery(email: String): String {
        return "SELECT FirstName, LastName, Phone FROM Contact WHERE Email = " + "'${email}'"
    }
}