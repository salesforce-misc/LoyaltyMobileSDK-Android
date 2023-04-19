package com.salesforce.loyalty.mobile.sources.forceUtils

import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse

interface ForceAuthenticator {

    var accessToken: String?

    suspend fun grantAccessToken(): String?

}