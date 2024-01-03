package com.salesforce.gamification

import com.salesforce.gamification.api.GameAuthenticator


object MockAuthenticator : GameAuthenticator {
    override fun getAccessToken(): String? {
        return "AccessToken1234"
    }

    override suspend fun grantAccessToken(): String? {
        return "AccessToken1234"
    }
}