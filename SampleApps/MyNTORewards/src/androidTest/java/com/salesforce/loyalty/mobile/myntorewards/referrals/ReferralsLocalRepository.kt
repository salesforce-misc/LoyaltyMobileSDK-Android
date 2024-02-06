package com.salesforce.loyalty.mobile.myntorewards.referrals

import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.MockResponseFileReader
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_PROGRAM_ID
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.repository.RecordList
import kotlinx.coroutines.delay

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
        //delay(1000)
        val gson = Gson()
        val mockResponse = MockResponseFileReader("ReferralEntityResponse.json").content

        var response= gson.fromJson(
            mockResponse,
            RecordList::class.java
        )
        var Obj = QueryResult<ReferralEntity>(13, true, response.records, "")
        return ApiResponse.Success(Obj)
    }

    suspend fun checkIfMemberEnrolled(promoCode: String, memberId: String): ApiResponse<QueryResult<ReferralEnrollmentInfo>> {

        val response = mockResponse("ReferralEnrollmentInfo.json", QueryResult::class.java) as QueryResult<ReferralEnrollmentInfo>
        delay(2000)
        return ApiResponse.Success(response)

    }

    suspend fun fetchMemberReferralCode(contactId: String): ApiResponse<QueryResult<ReferralCode>> {
        val response = mockResponse("ReferralEnrollmentInfo.json", QueryResult::class.java) as QueryResult<ReferralCode>
        return ApiResponse.Success(response)
    }

    private fun mockResponse(fileName: String, java: Class<*>): Any {
        val gson = Gson()
        val mockResponse =
            MockResponseFileReader(fileName).content
        return gson.fromJson(mockResponse, java)
    }
}