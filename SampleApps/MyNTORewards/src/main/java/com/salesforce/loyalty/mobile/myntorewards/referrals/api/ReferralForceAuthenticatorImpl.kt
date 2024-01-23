package com.salesforce.loyalty.mobile.myntorewards.referrals.api

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.referral.ReferralLogger
import com.salesforce.referral.api.ReferralForceAuthenticator

class ReferralForceAuthenticatorImpl(private val forceAuthManager: ForceAuthManager) : ReferralForceAuthenticator {
    override fun getAccessToken(): String? {
        ReferralLogger.d("ReferralForceAuthenticatorImpl", "getAccessToken")
        //TODO: Remove hard coded token
        return "00DB000000FX0aR!ARQAQKIdCPDOFmCuyIW_1r9fiRrSsvTvJJpgoSNoB57fQ9cOCF4o9SBCV6fROwUVoANPJT6AJYQ1IbQjqzlZblrw.nAeTO2b"
//        return forceAuthManager.getAccessToken()
    }

    override suspend fun grantAccessToken(): String? {
        ReferralLogger.d("ReferralForceAuthenticatorImpl", "grantAccessToken")
        return forceAuthManager.grantAccessToken()
    }
}