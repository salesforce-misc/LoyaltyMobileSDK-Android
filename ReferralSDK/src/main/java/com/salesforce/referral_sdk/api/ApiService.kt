package com.salesforce.referral_sdk.api

import com.salesforce.referral_sdk.entities.ReferralEnrollmentResponse
import com.salesforce.referral_sdk.entities.ReferralExistingEnrollmentRequest
import com.salesforce.referral_sdk.entities.ReferralNewEnrollmentRequestBody
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventRequest
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @POST
    suspend fun enrollNewCustomerAsAdvocateOfPromotion(
        @Url url: String,
        @Body requestBody: ReferralNewEnrollmentRequestBody
    ): Response<ReferralEnrollmentResponse>

    @POST
    suspend fun enrollExistingAdvocateToPromotion(
        @Url url: String,
        @Body requestBody: ReferralExistingEnrollmentRequest
    ): Response<ReferralEnrollmentResponse>

    @POST
    suspend fun sendReferrals(
        @Url url: String,
        @Body requestBody: ReferralEventRequest
    ): Response<ReferralEventResponse>
}