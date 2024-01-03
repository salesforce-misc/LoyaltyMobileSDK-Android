package com.salesforce.referral_sdk.api

import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralNewEnrollmentRequestBody
import com.salesforce.referral_sdk.entities.ReferralEnrollmentResponse
import com.salesforce.referral_sdk.entities.ReferralEntity
import com.salesforce.referral_sdk.entities.ReferralExistingEnrollmentRequest
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventRequest
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun fetchReferralsInfo(
        @Url url: String,
        @Query("q") query: String?,
        @Header("Authorization") bearerToken: String
    ): Response<QueryResult<ReferralEntity>>

    @POST
    suspend fun enrollNewCustomerAsAdvocateOfPromotion(
        @Url url: String,
        @Body requestBody: ReferralNewEnrollmentRequestBody,
        @Header("Authorization") bearerToken: String
    ): Response<ReferralEnrollmentResponse>

    @POST
    suspend fun enrollExistingAdvocateToPromotion(
        @Url url: String,
        @Body requestBody: ReferralExistingEnrollmentRequest,
        @Header("Authorization") bearerToken: String
    ): Response<ReferralEnrollmentResponse>

    @POST
    suspend fun sendReferrals(
        @Url url: String,
        @Body requestBody: ReferralEventRequest,
        @Header("Authorization") bearerToken: String
    ): Response<ReferralEventResponse>
}