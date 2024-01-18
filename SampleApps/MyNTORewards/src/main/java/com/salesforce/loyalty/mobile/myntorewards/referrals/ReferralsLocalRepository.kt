package com.salesforce.loyalty.mobile.myntorewards.referrals

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
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
                accessToken()
            )
        }
    }

    suspend fun checkIfMemberEnrolled(promoCode: String, memberId: String): ApiResponse<QueryResult<ReferralEnrollmentInfo>> {
        return safeApiCall {
            apiService.checkIfMemberEnrolled(
                sObjectUrl(),
                memberEnrollmentStatusQuery(promoCode, memberId),
                accessToken()
            )
        }
    }

    suspend fun fetchMemberReferralCode(accessToken: String, membershipNumber: String): ApiResponse<QueryResult<ReferralCode>> {
        return safeApiCall {
            apiService.fetchMemberReferralId(
                sObjectUrl(),
                memberReferralCodeQuery(membershipNumber),
                accessToken()
            )
        }
    }

    private fun accessToken() =
//        "Bearer ${forceAuthManager.getForceAuth()?.accessToken.orEmpty()}"
        "Bearer 00DB000000FX0aR!ARQAQCq1M5Io1M6LWzWyuXsARwcPp6Q0geoiFltINVya47W2DS1nwDGihssv5oWLtIn3s3qR4BFyQ.on8ju3ItoyVGf6a4Sf"

    private fun memberEnrollmentStatusQuery(promoCode: String, memberId: String) =
        "SELECT Id, Name, PromotionId, LoyaltyProgramMemberId FROM LoyaltyProgramMbrPromotion where LoyaltyProgramMemberId=\'$memberId\' and Promotion.PromotionCode=\'$promoCode\'"

    private fun memberReferralCodeQuery(membershipNumber: String) =
        "SELECT MembershipNumber, referralcode from loyaltyprogrammember where MembershipNumber =\'$membershipNumber\'"

    private fun referralListQuery(durationInDays: Int) =
        "SELECT Id, ClientEmail, ReferrerEmail, ReferralDate, CurrentPromotionStage.Type FROM Referral WHERE ReferralDate = LAST_N_DAYS:$durationInDays ORDER BY ReferralDate DESC"
}