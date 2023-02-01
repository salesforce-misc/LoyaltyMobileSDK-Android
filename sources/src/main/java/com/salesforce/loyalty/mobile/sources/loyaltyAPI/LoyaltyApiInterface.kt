package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentRequest
import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * LoyaltyApiInterface class holds the RESTAPI call definitions.
 */
interface LoyaltyApiInterface {

    @POST("individual-member-enrollments")
    suspend fun postEnrollment(
        @Body json: EnrollmentRequest
    ): Result<EnrollmentResponse>
}