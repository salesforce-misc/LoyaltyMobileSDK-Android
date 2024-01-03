package com.salesforce.loyalty.mobile.myntorewards.referrals

import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.safeApiCall
import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralEntity
import javax.inject.Inject

class ReferralsLocalRepository @Inject constructor(
    private val apiService: ReferralsLocalApiService,
    private val instanceUrl: String
) {

    companion object {
        const val SOQL_QUERY_PATH = "services/data/v"
        const val SOQL_QUERY_VERSION = "59.0"
        const val QUERY = "/query/"
    }

    private fun sObjectUrl() = instanceUrl + SOQL_QUERY_PATH + SOQL_QUERY_VERSION + QUERY

    suspend fun fetchReferralsInfo(accessToken: String, durationInDays: Int): ApiResponse<QueryResult<ReferralEntity>> {
        return safeApiCall {
            apiService.fetchReferralsInfo(
                sObjectUrl(),
                referralListQuery(durationInDays),
                "Bearer 00DSB000001oyRq!AQEAQKoL57FHmXhDjHkKusYINqe1YLuDCK.R.jJytSdtAthekOtlPsdRPaoqITEWpG5EiH0DyVmga2ijTn451SV9MgfTi._g"
            )
        }
    }

    suspend fun fetchMemberReferralCode(accessToken: String, membershipNumber: String): ApiResponse<QueryResult<ReferralCode>> {
        return safeApiCall {
            apiService.fetchMemberReferralId(
                sObjectUrl(),
                memberReferralCodeQuery(membershipNumber),
                "Bearer $accessToken"
            )
        }
    }

    private fun memberReferralCodeQuery(membershipNumber: String) =
        "SELECT MembershipNumber, referralcode from loyaltyprogrammember where MembershipNumber =\'$membershipNumber\'"

    private fun referralListQuery(durationInDays: Int) =
        "SELECT Id, ClientEmail, ReferrerEmail, ReferralDate, CurrentPromotionStage.Type FROM Referral WHERE ReferralDate = LAST_N_DAYS:$durationInDays ORDER BY ReferralDate DESC"
}