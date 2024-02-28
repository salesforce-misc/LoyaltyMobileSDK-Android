package com.salesforce.loyalty.mobile.myntorewards.referrals

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.salesforce.loyalty.mobile.myntorewards.MockResponseFileReader
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.repository.RecordList
import kotlinx.coroutines.delay
import java.lang.reflect.Type


class ReferralsLocalRepository constructor(
    private val apiService: ReferralsLocalApiService,
    private val instanceUrl: String
) {

    companion object {
        const val SOQL_QUERY_PATH = "/services/data/v"
        const val SOQL_QUERY_VERSION = "59.0"
        const val QUERY = "/query/"

        private var cachedReferralStatus = mutableMapOf<String, Pair<Boolean, Boolean>>()
        private var cachedPromoCode = mutableMapOf<String, Pair<String?, String?>>()

        fun clearReferralsData() {
            cachedReferralStatus = mutableMapOf()
            cachedPromoCode = mutableMapOf()
        }
    }

    private fun sObjectUrl() = instanceUrl + SOQL_QUERY_PATH + SOQL_QUERY_VERSION + QUERY

    suspend fun fetchReferralsInfo(contactId: String, durationInDays: Int): ApiResponse<QueryResult<ReferralEntity>> {
        //delay(1000)
        val bookType = object : TypeToken<QueryResult<ReferralEntity>>() {}.type
        val response = mockResponse("ReferralEntityResponse.json", bookType) as QueryResult<ReferralEntity>

//        val gson = Gson()
//        val mockResponse = MockResponseFileReader("ReferralEntityResponse.json").content
//
//        var response= gson.fromJson(
//            mockResponse,
//            RecordList::class.java
//        )
//        var Obj = QueryResult<ReferralEntity>(13, true, response.records, "")
        return ApiResponse.Success(response)
    }

    suspend fun checkIfMemberEnrolled(promoCode: String, memberId: String): ApiResponse<QueryResult<ReferralEnrollmentInfo>> {
        val bookType = object : TypeToken<QueryResult<ReferralEnrollmentInfo>>() {}.type

        val response = mockResponse("ReferralEnrollmentInfo.json", bookType) as QueryResult<ReferralEnrollmentInfo>
        delay(2000)
        return ApiResponse.Success(response)

    }

    suspend fun fetchMemberReferralCode(contactId: String, programName: String = ReferralConfig.REFERRAL_PROGRAM_NAME): ApiResponse<QueryResult<ReferralCode>> {
        val bookType = object : TypeToken<QueryResult<ReferralCode>>() {}.type
        val response = mockResponse("ReferralCode.json", bookType) as QueryResult<ReferralCode>
        return ApiResponse.Success(response)
    }

    suspend fun checkIfGivenPromotionIsReferralAndEnrolled(promoId: String): ApiResponse<QueryResult<ReferralPromotionStatusAndPromoCode>> {
        val bookType = object : TypeToken<QueryResult<ReferralPromotionStatusAndPromoCode>>() {}.type

        val response = mockResponse("PromotionTypeAndCode.json", bookType) as QueryResult<ReferralPromotionStatusAndPromoCode>
        val results = listOf<ReferralPromotionStatusAndPromoCode>(
            ReferralPromotionStatusAndPromoCode(
                "Default Referral Promotion",
                "TEMPRP7",
                "https://rb.gy/wa6jw7?referralCode=",
                true
            )
        )
        var Obj = QueryResult<ReferralPromotionStatusAndPromoCode>(1, true, results, "")

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
        cachedPromoCode[promotionId] = Pair(promotionDetails?.promotionCode, promotionDetails?.promotionPageUrl)
    }

    fun getPromoCodeFromCache(promotionId: String): String? {
        return cachedPromoCode[promotionId]?.first
    }

    fun getPromoUrlFromCache(promotionId: String): String {
        return cachedPromoCode[promotionId]?.second.orEmpty()
    }

    private fun mockResponse(fileName: String, java: Type): Any {
        val gson = Gson()
        val mockResponse =
            MockResponseFileReader(fileName).content
        return gson.fromJson(mockResponse, java)
    }
}