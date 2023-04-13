package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.util.Log
import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * ForceAuthManager class handles authentication of Salesforce credentials
 */
object ForceAuthManager : ForceAuthenticator {

    private const val TAG = "ForceAuthManager"
    private const val AUTH_SCOPE = "api refresh_token"
    private const val RESPONSE_TYPE_VALUE = "code_credentials"

    override var accessToken: String? = null
    var auth: ForceAuth? = null

    override suspend fun grantAccessToken(): String? {
        Log.d(TAG, "grantAccessToken()")
        auth?.let {
            // Refresh access token
            refreshAccessToken(
                ForceConfig.getRefreshAccessTokenRequestUrl(ForceConfig.ACCESS_TOKEN_BASE_URL),
                ConnectedAppConfig.CONSUMER_KEY,
                ConnectedAppConfig.CONSUMER_SECRET,
                it
            )
        }
        return grantAuth()
    }

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
        }
        return accessToken
    }
    suspend fun authenticate(
        communityUrl: String,
        consumerKey: String,
        callbackUrl: String,
        userName: String,
        password: String
    ): String? {

        try {
            requestAuthorizationCode(
                communityUrl,
                consumerKey,
                callbackUrl,
                userName,
                password
            )
        } catch (ex: HttpException) {
            Log.d(TAG, "Access token HttpException")
            val responseCode = ex.response()?.code()
            if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val redirectUrl = ex.response()?.raw()?.request?.url
                redirectUrl?.let {
                    val code = getAuthorizationCode(it)
                    Log.d(TAG, "Auth code : $code")
                    code?.let { authCode ->
                        return requestAccessToken(
                            ConnectedAppConfig.COMMUNITY_URL,
                            authCode,
                            ConnectedAppConfig.CONSUMER_KEY,
                            ConnectedAppConfig.CALLBACK_URL,
                        )
                    }
                }
            }
        }
        return null
    }

    private fun getAuthorizationCode(url: HttpUrl): String? {
        val queryItems = url.queryParameter("code")
        return queryItems?.replace("%3D", "=")
    }
    private suspend fun requestAuthorizationCode(communityUrl: String, consumerKey: String, callbackURL: String, userName: String, password: String) {

        ForceClient.authApi.requestAuthorizationCode(
            ForceConfig.getAuthorizationCodeRequestUrl(communityUrl),
            ForceConfig.HEADER_AUTH_REQUEST_TYPE_VALUE,
            AUTH_SCOPE,
            RESPONSE_TYPE_VALUE,
            ConnectedAppConfig.CONSUMER_KEY,
            ConnectedAppConfig.CALLBACK_URL,
            userName,
            password
        )

    }

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

    private fun saveAuth(auth: ForceAuth){
        this.auth = auth
    }
}