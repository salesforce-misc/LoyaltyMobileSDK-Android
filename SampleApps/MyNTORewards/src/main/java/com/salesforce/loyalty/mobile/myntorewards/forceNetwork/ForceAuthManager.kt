package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.content.Context
import android.util.Log
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import okhttp3.HttpUrl
import retrofit2.HttpException
import java.net.HttpURLConnection

/**
 * ForceAuthManager class handles authentication of Salesforce credentials
 */
object ForceAuthManager: ForceAuthenticator {

    lateinit var forceAuthManager: ForceAuthManager

    lateinit var mContext: Context

    fun getInstance(context: Context): ForceAuthManager {
        if (!::forceAuthManager.isInitialized) {
            mContext = context
            forceAuthManager = ForceAuthManager
        }
        return forceAuthManager
    }

    private const val TAG = "ForceAuthManager"
    private const val AUTH_SCOPE = "api refresh_token"
    private const val RESPONSE_TYPE_VALUE = "code_credentials"

    var auth: ForceAuth? = null
    override fun getAccessToken(): String? {
        val forceAuth = getForceAuth()
        forceAuth?.let {
            return it.accessToken
        }
        return null
    }

    override suspend fun grantAccessToken(): String? {
        Log.d(TAG, "grantAccessToken()")
        val selectedConnectedApp = PrefHelper.customPrefs(mContext)
            .get<String>(AppConstants.KEY_SELECTED_CONNECTED_APP_NAME, null)
        val connectedApp = selectedConnectedApp?.let { selectedConnectedApp ->
            ForceConnectedAppEncryptedPreference.getConnectedApp(
                mContext,
                selectedConnectedApp
            )
        } ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP
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
     * OAuth 2.0 Username-Password Flow - Use username and password behind screen to obtain a valid accessToken
     * https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_username_password_flow.htm&type=5
     */
    suspend fun grantAuth(): String? {
        val response = ForceClient.authApi.getAccessToken(
            ForceConfig.MimeType.JSON,
            ForceConfig.MimeType.FORM_ENCODED,
            ForceConfig.ADMIN_USERNAME,
            ForceConfig.ADMIN_PASSWORD,
            ForceConfig.GRANT_TYPE_PASSWORD,
            ForceConfig.CONSUMER_KEY,
            ForceConfig.CONSUMER_SECRET
        )
        response.onSuccess {
            saveAuth(it)
            return it.accessToken
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
            Log.d(TAG, "Access token HttpException ${ex.message}")
            val responseCode = ex.response()?.code()
            if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val redirectUrl = ex.response()?.raw()?.request?.url
                redirectUrl?.let {
                    val code = getAuthorizationCode(it)
                    Log.d(TAG, "Auth code : $code")
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
            Log.d(TAG, "Failed to generate access token ${ex.message}")
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
            Log.d(TAG, "Access token success: ${it}")
            saveAuth(it)
            return it.accessToken
        }.onFailure {
            Log.d(TAG, "Access token failure : ${it.message}")
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

        Log.d(TAG, "No auth found. Please login.")
        return null
    }
}