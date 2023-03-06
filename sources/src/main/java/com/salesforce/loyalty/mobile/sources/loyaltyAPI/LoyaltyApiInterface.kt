package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
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

    @GET()
    suspend fun getTransactions(
        @Url url: String,
        @Query("pageNumber") pageNumber: Int?,
        @Query("journalTypeName") journalTypeName: String?,
        @Query("journalSubTypeName") journalSubTypeName: String?,
        @Query("periodStartDate") periodStartDate: String?,
        @Query("periodEndDate") periodEndDate: String?
    ): Result<TransactionsResponse>

    @POST()
    suspend fun getEligiblePromotions(
        @Url url: String,
        @Body requestBody: PromotionsRequest
    ): Result<PromotionsResponse>

    @POST()
    suspend fun enrollInPromotion(
        @Url url: String,
        @Body requestBody: PromotionsRequest
    ): Result<EnrollPromotionsResponse>

    @POST()
    suspend fun unenrollPromotion(
        @Url url: String,
        @Body requestBody: PromotionsRequest
    ): Result<UnenrollPromotionResponse>

    @GET()
    suspend fun getVouchers(
        @Url url: String,
        @Query("voucherStatus") voucherStatus: String?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("productId") productId: String?,
        @Query("productCategoryId") productCategoryId: String?,
        @Query("productName") productName: String?,
        @Query("productCategoryName") productCategoryName: String?
    ): Result<VoucherResponse>
}