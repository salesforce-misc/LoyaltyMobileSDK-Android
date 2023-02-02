package com.salesforce.loyalty.mobile.sources.forceUtils

import android.util.Log
import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse

/**
 * ForceAuthManager class handles authentication of Salesforce credentials
 */
object ForceAuthManager {

    private const val TAG = "ForceAuthManager"

    suspend fun getAccessToken(): Result<ForceAuthResponse> {
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
            ForceAuth.setAuthToken(it.accessToken)
        }
        return response
    }
}