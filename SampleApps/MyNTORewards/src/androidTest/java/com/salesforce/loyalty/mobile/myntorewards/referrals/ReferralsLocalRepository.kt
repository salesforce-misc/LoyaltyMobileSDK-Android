package com.salesforce.loyalty.mobile.myntorewards.referrals

import android.util.Log
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.MockResponseFileReader
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_PROGRAM_ID
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.RecordList
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.api.safeApiCall
import kotlinx.coroutines.delay

import javax.inject.Inject

class ReferralsLocalRepository constructor(
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

    suspend fun fetchReferralsInfo(contactId: String, promoCode: String, durationInDays: Int): ApiResponse<QueryResult<ReferralEntity>> {
        delay(1000)
        val gson = Gson()
        val mockResponse = MockResponseFileReader("refferralData.json").content

        var response= gson.fromJson(
            mockResponse,
            RecordList::class.java
        )
        var Obj = QueryResult<ReferralEntity>(13, true, response.records, "")
        return ApiResponse.Success(Obj)
    }

    suspend fun checkIfMemberEnrolled(promoCode: String, memberId: String): ApiResponse<QueryResult<ReferralEnrollmentInfo>> {
        val gson = Gson()
        val mockResponse = MockResponseFileReader("IsMemberEnrolled.json").content
        var response= gson.fromJson(
            mockResponse,
            QueryResult::class.java
        ) as QueryResult<ReferralEnrollmentInfo>

        delay(2000)
        return ApiResponse.Success(response)
    }

    suspend fun fetchMemberReferralCode(contactId: String): ApiResponse<QueryResult<ReferralCode>> {
        val gson = Gson()
        val mockResponse = MockResponseFileReader("IsMemberEnrolled.json").content
        var response= gson.fromJson(
            mockResponse,
            QueryResult::class.java
        ) as QueryResult<ReferralCode>
        return ApiResponse.Success(response)
    }

    private fun accessToken() =
//        "Bearer ${forceAuthManager.getForceAuth()?.accessToken.orEmpty()}"
        // TODO: Replace hard coded token
        "Bearer 00DB000000FX0aR!ARQAQNf3gSx28wnWtCeESz2GtZRwboZQlEC6bGfQ_UMGvUSxZ8pljA2_QWa7FrIP5sxLevZ9qTQnYHu2.gf1LguDbG54Dwm1"

    private fun memberEnrollmentStatusQuery(promoCode: String, memberId: String) =
        "SELECT Id, Name, PromotionId, LoyaltyProgramMemberId FROM LoyaltyProgramMbrPromotion where LoyaltyProgramMemberId=\'$memberId\' and Promotion.PromotionCode=\'$promoCode\'"

    private fun memberReferralCodeQuery(contactId: String) =
        "SELECT MembershipNumber, ContactId, ProgramId, ReferralCode FROM LoyaltyProgramMember where contactId = '$contactId' and ProgramId='$REFERRAL_PROGRAM_ID'"

    private fun referralListQuery(contactId: String, promoCode: String, durationInDays: Int) =
        "SELECT ReferredPartyId, ReferralDate, CurrentPromotionStage.Type, TYPEOF ReferredParty WHEN Contact THEN Account.PersonEmail WHEN Account THEN PersonEmail END FROM Referral WHERE Promotion.PromotionCode=\'$promoCode\' and ReferrerId = \'$contactId\' and ReferralDate = LAST_N_DAYS:$durationInDays ORDER BY ReferralDate DESC"
}