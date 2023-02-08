package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentRequest
import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * LoyaltyApiInterface class holds the RESTAPI call definitions.
 */
interface LoyaltyApiInterface {

    @POST()
    suspend fun postEnrollment(
        @Url url: String,
        @Body json: EnrollmentRequest
    ): Result<EnrollmentResponse>
}