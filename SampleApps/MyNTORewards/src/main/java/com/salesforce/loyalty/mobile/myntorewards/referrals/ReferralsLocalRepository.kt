package com.salesforce.loyalty.mobile.myntorewards.referrals

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.safeApiCall
import com.salesforce.referral_sdk.entities.QueryResult
import javax.inject.Inject

class ReferralsLocalRepository @Inject constructor(
    private val apiService: ReferralsLocalApiService,
    private val instanceUrl: String,
    private val forceAuthManager: ForceAuthManager
) {

    companion object {
        const val SOQL_QUERY_PATH = "/services/data/v"
        const val SOQL_QUERY_VERSION = "59.0"
        const val QUERY = "/query/"
    }

    private fun sObjectUrl() = instanceUrl + SOQL_QUERY_PATH + SOQL_QUERY_VERSION + QUERY

    suspend fun fetchReferralsInfo(accessToken: String, durationInDays: Int): ApiResponse<QueryResult<ReferralEntity>> {
        return safeApiCall {
            apiService.fetchReferralsInfo(
                sObjectUrl(),
                referralListQuery(durationInDays),
            )
        }
    }

    suspend fun fetchMemberReferralCode(accessToken: String, membershipNumber: String): ApiResponse<QueryResult<ReferralCode>> {
        return safeApiCall {
            apiService.fetchMemberReferralId(
                sObjectUrl(),
                memberReferralCodeQuery(membershipNumber),
                "Bearer 00DB000000FX0aR!ARQAQM0qRWUqU9vwYZf5yVnS8AXiruTKUT2YtpxpyONqgB5CQkkQCzZZOjY8xxBeFRS07YYG5SEwTyfI91g12gC5CMf.utNx"
            )
        }
    }

    private fun accessToken() = forceAuthManager.getForceAuth()?.accessToken.orEmpty()

    private fun memberReferralCodeQuery(membershipNumber: String) =
        "SELECT MembershipNumber, referralcode from loyaltyprogrammember where MembershipNumber =\'$membershipNumber\'"

    private fun referralListQuery(durationInDays: Int) =
        "SELECT Id, ClientEmail, ReferrerEmail, ReferralDate, CurrentPromotionStage.Type FROM Referral WHERE ReferralDate = LAST_N_DAYS:$durationInDays ORDER BY ReferralDate DESC"
}