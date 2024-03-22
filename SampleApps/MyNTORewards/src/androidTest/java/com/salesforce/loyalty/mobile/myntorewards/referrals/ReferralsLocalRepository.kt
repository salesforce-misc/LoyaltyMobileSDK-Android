package com.salesforce.loyalty.mobile.myntorewards.referrals

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.salesforce.loyalty.mobile.myntorewards.mockResponse
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnablementStatus
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import com.salesforce.referral.api.ApiResponse
import kotlinx.coroutines.delay
import retrofit2.Response


class ReferralsLocalRepository constructor(
    private val apiService: ReferralsLocalApiService,
    private val instanceUrl: String
) {

    companion object {
        private var cachedReferralStatus = mutableMapOf<String, Pair<Boolean, Boolean>>()
        private var cachedPromoCode = mutableMapOf<String, ReferralPromotionStatusAndPromoCode>()
        private var isReferralPromotion: Boolean = true
        private var isReferralsListApiSuccess: Boolean = true
        private var isReferralEnrollmentStatusApiSuccess: Boolean = true
        private var isPromotionTypeAndCodeApiSuccess: Boolean = true
        private var isReferralFeatureEnabled: Boolean? = null
        private var endDate: String? = "2030-12-31"

        fun clearReferralsData() {
            cachedReferralStatus = mutableMapOf()
            cachedPromoCode = mutableMapOf()
            isReferralPromotion = true
            isReferralsListApiSuccess = true
            isReferralEnrollmentStatusApiSuccess = true
            isPromotionTypeAndCodeApiSuccess = true
            isReferralFeatureEnabled = null
        }
    }

    suspend fun fetchReferralsInfo(contactId: String, durationInDays: Int): ApiResponse<QueryResult<ReferralEntity>> {
        val responseType = object : TypeToken<QueryResult<ReferralEntity>>() {}.type
        val response = mockResponse("ReferralEntityResponse.json", responseType) as QueryResult<ReferralEntity>
        return if (isReferralsListApiSuccess) {
            ApiResponse.Success(response)
        } else {
            ApiResponse.NetworkError
        }
    }

    suspend fun checkIfMemberEnrolled(promoCode: String, memberId: String): ApiResponse<QueryResult<ReferralEnrollmentInfo>> {
        val responseType = object : TypeToken<QueryResult<ReferralEnrollmentInfo>>() {}.type
        val response = mockResponse("ReferralEnrollmentInfo.json", responseType) as QueryResult<ReferralEnrollmentInfo>
        delay(2000)
        return if (isReferralEnrollmentStatusApiSuccess) {
            ApiResponse.Success(response)
        } else {
            ApiResponse.NetworkError
        }
    }

    suspend fun fetchMemberReferralCode(contactId: String, programName: String = ReferralConfig.REFERRAL_PROGRAM_NAME): ApiResponse<QueryResult<ReferralCode>> {
        val responseType = object : TypeToken<QueryResult<ReferralCode>>() {}.type
        val response = mockResponse("ReferralCode.json", responseType) as QueryResult<ReferralCode>
        return ApiResponse.Success(response)
    }

    suspend fun checkIfGivenPromotionIsReferralAndEnrolled(promoId: String): ApiResponse<QueryResult<ReferralPromotionStatusAndPromoCode>> {
        val results = listOf(
            ReferralPromotionStatusAndPromoCode(
                "Default Referral Promotion",
                "TEMPRP7",
                "https://rb.gy/wa6jw7",
                "Invite your friends and get a voucher when they shop for the first time.",
                endDate,
                "https://rb.gy/wa6jw7",
                isReferralPromotion
            )
        )
        var customResponse = QueryResult(1, true, results, "")
        return if (isPromotionTypeAndCodeApiSuccess) {
            ApiResponse.Success(customResponse)
        } else {
            ApiResponse.NetworkError
        }
    }

    suspend fun checkIfReferralIsEnabled(): ApiResponse<QueryResult<ReferralEnablementStatus>> {
        val responseType = object : TypeToken<QueryResult<ReferralEnablementStatus>>() {}.type
        val response = mockResponse("ReferralEnrollmentInfo.json", responseType) as QueryResult<ReferralEnablementStatus>
        return ApiResponse.Success(response)
    }

    fun getDefaultPromotionDetailsFromCache(context: Context, memberShipNumber: String, promotionId: String): Results? {
        val response = mockResponse("Promotions.json", PromotionsResponse::class.java) as PromotionsResponse
        return response.outputParameters?.outputParameters?.results?.firstOrNull { it.promotionId == promotionId }
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

    fun getPromoCodeFromCache(promotionId: String): String? {
        return cachedPromoCode[promotionId]?.promotionCode
    }

    fun getPromoUrlFromCache(promotionId: String): String {
        return cachedPromoCode[promotionId]?.promotionPageUrl.orEmpty()
    }

    fun getReferralPromotionDetails(promotionId: String): ReferralPromotionStatusAndPromoCode? {
        return cachedPromoCode[promotionId]
    }

    fun setReferralFeatureEnabled(enabled: Boolean) {
        isReferralFeatureEnabled = enabled
    }

    fun isReferralFeatureEnabled(): Boolean? {
        return isReferralFeatureEnabled
    }

    fun setPromotionType(isReferral: Boolean) {
        isReferralPromotion = isReferral
    }

    fun setReferralsListApiSuccess(success: Boolean) {
        isReferralsListApiSuccess = success
    }

    fun setReferralEnrollmentStatusApiSuccess(success: Boolean) {
        isReferralEnrollmentStatusApiSuccess = success
    }

    fun setPromotionTypeAndCodeApiSuccess(success: Boolean) {
        isPromotionTypeAndCodeApiSuccess = success
    }

    fun setPromotionExpiredDate(date: String) {
        endDate = date
    }
}