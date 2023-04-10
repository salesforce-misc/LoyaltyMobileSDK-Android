package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.util.Log
import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator

/**
 * ForceAuthManager class handles authentication of Salesforce credentials
 */
object ForceAuthManager : ForceAuthenticator {

    private const val TAG = "ForceAuthManager"

    override var accessToken: String? = null

    override suspend fun grantAccessToken(): Result<ForceAuthResponse> {
        Log.d(TAG, "getAccessToken()")
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
            accessToken = it.accessToken
        }
        return response
    }
}