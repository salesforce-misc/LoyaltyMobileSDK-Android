package com.salesforce.loyalty.mobile.sources.forceUtils

import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse

interface ForceAuthenticator {

    fun getAccessToken(): String?

    suspend fun grantAccessToken(): String?

}