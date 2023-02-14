package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentRequest
import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefitsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberProfileResponse
import retrofit2.http.*

/**
 * LoyaltyApiInterface class holds the RESTAPI call definitions.
 */
interface LoyaltyApiInterface {

    @POST()
    suspend fun postEnrollment(
        @Url url: String,
        @Body json: EnrollmentRequest
    ): Result<EnrollmentResponse>

    @GET()
    suspend fun getMemberProfile(
        @Url url: String,
        @Query("memberId") memberId: String?,
        @Query("membershipNumber") membershipNumber: String?,
        @Query("programCurrencyName") programCurrencyName: String?
    ): Result<MemberProfileResponse>

    @GET()
    suspend fun getMemberBenefits(
        @Url url: String,
        @Query("membershipNumber") membershipNumber: String,
    ): Result<MemberBenefitsResponse>
}