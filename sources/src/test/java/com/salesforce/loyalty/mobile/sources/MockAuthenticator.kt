package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator

object MockAuthenticator : ForceAuthenticator {
    override fun getAccessToken(): String? {
        return "AccessToken1234"
    }

    override suspend fun grantAccessToken(): String? {
        return "AccessToken1234"
    }
}