package com.salesforce.loyalty.mobile.myntorewards.referrals.api

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.referral.ReferralLogger
import com.salesforce.referral.api.ReferralForceAuthenticator

class ReferralForceAuthenticatorImpl(private val forceAuthManager: ForceAuthManager) : ReferralForceAuthenticator {
    override fun getAccessToken(): String? {
        ReferralLogger.d("ReferralForceAuthenticatorImpl", "getAccessToken")
        //TODO: Remove hard coded token
        return "00DB000000FX0aR!ARQAQLOe8NMiyzP592U1KwUYExvHtPps4i3YM5bMkr5AJ2w4zjuxs3anWZ5LK2.zOC_8QUXkAKnW2kSYe4uosfb7UGN6TFhQ"
//        return forceAuthManager.getAccessToken()
    }

    override suspend fun grantAccessToken(): String? {
        ReferralLogger.d("ReferralForceAuthenticatorImpl", "grantAccessToken")
        return forceAuthManager.grantAccessToken()
    }
}