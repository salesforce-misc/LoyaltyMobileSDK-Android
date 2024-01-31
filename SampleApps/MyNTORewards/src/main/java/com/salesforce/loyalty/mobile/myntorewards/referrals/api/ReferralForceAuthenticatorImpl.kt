package com.salesforce.loyalty.mobile.myntorewards.referrals.api

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.referral.ReferralLogger
import com.salesforce.referral.api.ReferralForceAuthenticator

class ReferralForceAuthenticatorImpl(private val forceAuthManager: ForceAuthManager) : ReferralForceAuthenticator {
    override fun getAccessToken(): String? {
        ReferralLogger.d("ReferralForceAuthenticatorImpl", "getAccessToken")
        //TODO: Remove hard coded token
        return "00DB000000FX0aR!ARQAQNf3gSx28wnWtCeESz2GtZRwboZQlEC6bGfQ_UMGvUSxZ8pljA2_QWa7FrIP5sxLevZ9qTQnYHu2.gf1LguDbG54Dwm1"
//        return forceAuthManager.getAccessToken()
    }

    override suspend fun grantAccessToken(): String? {
        ReferralLogger.d("ReferralForceAuthenticatorImpl", "grantAccessToken")
        return forceAuthManager.grantAccessToken()
    }
}