package com.salesforce.loyalty.mobile.myntorewards.referrals.api

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.referral_sdk.Logger
import com.salesforce.referral_sdk.api.ForceAuthenticator

class ReferralForceAuthenticatorImpl(private val forceAuthManager: ForceAuthManager) : ForceAuthenticator {
    override fun getAccessToken(): String? {
        Logger.d("ReferralForceAuthenticatorImpl", "getAccessToken")
        return forceAuthManager.getAccessToken()
    }

    override suspend fun grantAccessToken(): String? {
        Logger.d("ReferralForceAuthenticatorImpl", "grantAccessToken")
        return forceAuthManager.grantAccessToken()
    }
}