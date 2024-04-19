package com.salesforce.loyalty.mobile.myntorewards.referrals

import android.content.Context
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_PROGRAM_NAME
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnablementStatus
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralsInfoEntity
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.api.safeApiCall

import javax.inject.Inject

/**
 * Repository class to make the SOQL query APIs and to cache the data related to referrals feature
 */
class ReferralsLocalRepository @Inject constructor(
    private val apiService: ReferralsLocalApiService,
    private val instanceUrl: String
) {


    companion object {
        const val SOQL_QUERY_PATH = "/services/data/v"
        const val SOQL_QUERY_VERSION = "59.0"
        const val QUERY = "/query/"

        private var cachedReferralStatus = mutableMapOf<String, Pair<Boolean, Boolean>>()
        private var cachedPromoCode = mutableMapOf<String, ReferralPromotionStatusAndPromoCode>()
        private var isReferralFeatureEnabled: Boolean? = null
        fun clearReferralsData() {
            cachedReferralStatus = mutableMapOf()
            cachedPromoCode = mutableMapOf()
            isReferralFeatureEnabled = null
        }
    }

    private fun sObjectUrl() = instanceUrl + SOQL_QUERY_PATH + SOQL_QUERY_VERSION + QUERY

    suspend fun fetchReferralsInfo(contactId: String, durationInDays: Int): ApiResponse<ReferralsInfoEntity> {
        return safeApiCall {
            apiService.fetchReferralsInfo(
                referralListQuery(contactId, durationInDays)
            )
        }
    }

    suspend fun checkIfMemberEnrolled(promoCode: String, memberId: String): ApiResponse<QueryResult<ReferralEnrollmentInfo>> {
        return safeApiCall {
            apiService.checkIfMemberEnrolled(
                sObjectUrl(),
                memberEnrollmentStatusQuery(promoCode, memberId)
            )
        }
    }

    suspend fun checkIfGivenPromotionIsReferralAndEnrolled(promoId: String): ApiResponse<QueryResult<ReferralPromotionStatusAndPromoCode>> {
        return safeApiCall {
            apiService.checkIfGivenPromotionIsReferralAndEnrolled(
                sObjectUrl(),
                memberEnrollmentAndReferralStatusQuery(promoId)
            )
        }
    }

    suspend fun fetchMemberReferralCode(contactId: String, programName: String = REFERRAL_PROGRAM_NAME): ApiResponse<QueryResult<ReferralCode>> {
        return safeApiCall {
            apiService.fetchMemberReferralId(
                sObjectUrl(),
                memberReferralCodeQuery(contactId, programName)
            )
        }
    }

    suspend fun checkIfReferralIsEnabled(): ApiResponse<QueryResult<ReferralEnablementStatus>> {
        return safeApiCall {
            apiService.checkIfReferralIsEnabled(
                sObjectUrl(),
                memberEnrollmentAndReferralStatusQuery()
            )
        }
    }

    private fun memberEnrollmentAndReferralStatusQuery() =
        "SELECT FIELDS(ALL) FROM Promotion LIMIT 1"

    private fun memberEnrollmentAndReferralStatusQuery(promoId: String) =
        "SELECT Id, IsReferralPromotion, PromotionCode, PromotionPageUrl, Name, Description, EndDate, ImageUrl FROM Promotion Where Id= \'$promoId\'"

    private fun memberEnrollmentStatusQuery(promoCode: String, contactId: String) =
        "SELECT Id, Name, PromotionId, LoyaltyProgramMemberId, LoyaltyProgramMember.ContactId FROM LoyaltyProgramMbrPromotion where LoyaltyProgramMember.ContactId=\'$contactId\' and Promotion.PromotionCode=\'$promoCode\'"

    private fun memberReferralCodeQuery(contactId: String, programName: String) =
        "SELECT MembershipNumber, ContactId, ProgramId, ReferralCode FROM LoyaltyProgramMember where contactId = '$contactId' and Program.Name='$programName'"

    /**
     * APEX api to fetch Referrals list info
     * @param membershipNumber - Member ship number of the Referral Program
     * @param durationInDays - Fetch referrals data only for the given no.of days
     */
    private fun referralListQuery(membershipNumber: String, durationInDays: Int) =
        "$instanceUrl/services/apexrest/get-referral-details/?membershipnumber=$membershipNumber&noOfDays=$durationInDays"

    fun getDefaultPromotionDetailsFromCache(context: Context, memberShipNumber: String, promotionId: String): Results? {
        val promotionCache = LocalFileManager.getData(
            context,
            memberShipNumber,
            LocalFileManager.DIRECTORY_PROMOTIONS,
            PromotionsResponse::class.java
        )
        val promotionsList = promotionCache?.outputParameters?.outputParameters?.results
        return promotionsList?.firstOrNull { it.promotionId == promotionId }
    }

    fun saveReferralStatusInCache(promotionCode: String, isReferralPromotion: Boolean, isEnrolled: Boolean) {
        cachedReferralStatus[promotionCode] = Pair(isReferralPromotion, isEnrolled)
    }

    fun getReferralStatusFromCache(promotionCode: String): Pair<Boolean, Boolean> {
        return cachedReferralStatus[promotionCode] ?: Pair(false, false)
    }

    fun savePromoCodeAndUrlInCache(promotionId: String, promotionDetails: ReferralPromotionStatusAndPromoCode?) {
        promotionDetails?.let {
            cachedPromoCode[promotionId] = it
        }
    }

    fun getReferralPromotionDetails(promotionId: String): ReferralPromotionStatusAndPromoCode? {
        return cachedPromoCode[promotionId]
    }

    fun getPromoCodeFromCache(promotionId: String): String? {
        return cachedPromoCode[promotionId]?.promotionCode
    }

    fun getPromoUrlFromCache(promotionId: String): String {
        return cachedPromoCode[promotionId]?.promotionPageUrl.orEmpty()
    }

    fun setReferralFeatureEnabled(enabled: Boolean) {
        isReferralFeatureEnabled = enabled
    }

    fun isReferralFeatureEnabled(): Boolean? {
        return isReferralFeatureEnabled
    }

}