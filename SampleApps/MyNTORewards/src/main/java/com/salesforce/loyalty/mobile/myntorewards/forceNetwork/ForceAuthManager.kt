package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
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

    override var accessToken: String? = null
    var auth: ForceAuth? = null

    override suspend fun grantAccessToken(): String? {
        Log.d(TAG, "grantAccessToken()")
        auth?.let {
            // Refresh access token
            val selectedConnectedApp = PrefHelper.customPrefs(mContext)
                .get<String>(AppConstants.KEY_SELECTED_CONNECTED_APP_NAME, null)
            val connectedApp = selectedConnectedApp?.let {
                ForceConnectedAppEncryptedPreference.getConnectedApp(
                    mContext,
                    it
                )
            } ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP
            refreshAccessToken(
                url = ForceConfig.getRefreshAccessTokenRequestUrl(connectedApp.communityUrl),
                consumerKey = connectedApp.consumerKey,
                consumerSecret = connectedApp.consumerSecret,
                it
            )
        }
        return grantAuth()
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
            auth = it
            accessToken = it.accessToken
            ForceAuthEncryptedPreference.saveAUth(mContext, it)
        }
        return accessToken
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
            Log.d(TAG, "Access token success: ${it.accessToken}")
            accessToken = it.accessToken
            auth = it
        }.onFailure {
            Log.d(TAG, "Access token failure : ${it.message}")
        }
        return accessToken
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
        auth: ForceAuth
    ): ForceAuth? {
        val response = ForceClient.authApi.refreshAccessToken(
            url,
            ForceConfig.REFRESH_GRANT_TYPE,
            consumerKey,
            auth.refreshToken!!,
            consumerSecret
        )
        response.onSuccess {
            accessToken = it.accessToken
            saveAuth(it)
            return it
        }.onFailure {
            return null
        }
        return null
    }

    private fun saveAuth(auth: ForceAuth) {
        this.auth = auth
    }
}