package com.salesforce.loyalty.mobile.myntorewards.referrals

import android.content.Context
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_PROGRAM_NAME
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.api.safeApiCall

import javax.inject.Inject

class ReferralsLocalRepository @Inject constructor(
    private val apiService: ReferralsLocalApiService,
    private val instanceUrl: String
) {


    companion object {
        const val SOQL_QUERY_PATH = "/services/data/v"
        const val SOQL_QUERY_VERSION = "59.0"
        const val QUERY = "/query/"

        private var cachedReferralStatus = mutableMapOf<String, Pair<Boolean, Boolean>>()
        private var cachedPromoCode = mutableMapOf<String, String>()

        fun clearReferralsData() {
            cachedReferralStatus = mutableMapOf()
            cachedPromoCode = mutableMapOf()
        }
    }

    private fun sObjectUrl() = instanceUrl + SOQL_QUERY_PATH + SOQL_QUERY_VERSION + QUERY

    suspend fun fetchReferralsInfo(contactId: String, durationInDays: Int): ApiResponse<QueryResult<ReferralEntity>> {
        return safeApiCall {
            apiService.fetchReferralsInfo(
                sObjectUrl(),
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



    private fun memberEnrollmentAndReferralStatusQuery(promoId: String) =
        "SELECT Id, IsReferralPromotion, PromotionCode, Name FROM Promotion Where Id= \'$promoId\'"

    private fun memberEnrollmentStatusQuery(promoCode: String, contactId: String) =
        "SELECT Id, Name, PromotionId, LoyaltyProgramMemberId, LoyaltyProgramMember.ContactId FROM LoyaltyProgramMbrPromotion where LoyaltyProgramMember.ContactId=\'$contactId\' and Promotion.PromotionCode=\'$promoCode\'"

    private fun memberReferralCodeQuery(contactId: String, programName: String) =
        "SELECT MembershipNumber, ContactId, ProgramId, ReferralCode FROM LoyaltyProgramMember where contactId = '$contactId' and Program.Name='$programName'"

    private fun referralListQuery(contactId: String, durationInDays: Int) =
        "SELECT ReferredPartyId, ReferralDate, CurrentPromotionStage.Type, TYPEOF ReferredParty WHEN Contact THEN Account.PersonEmail WHEN Account THEN PersonEmail END FROM Referral WHERE ReferrerId = \'$contactId\' and ReferralDate = LAST_N_DAYS:$durationInDays ORDER BY ReferralDate DESC"

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

    fun setReferralStatus(promotionCode: String, isReferralPromotion: Boolean, isEnrolled: Boolean) {
        cachedReferralStatus[promotionCode] = Pair(isReferralPromotion, isEnrolled)
    }

    fun getReferralStatus(promotionCode: String): Pair<Boolean, Boolean> {
        return cachedReferralStatus[promotionCode] ?: Pair(false, false)
    }

    fun setPromoCode(promotionId: String, promotionCode: String) {
        cachedPromoCode[promotionId] = promotionCode
    }

    fun getPromoCodeFromCache(promotionId: String): String? {
        return cachedPromoCode[promotionId]
    }
}