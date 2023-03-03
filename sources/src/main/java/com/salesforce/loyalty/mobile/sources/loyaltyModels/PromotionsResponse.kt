package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * PromotionsResponse data class holds response parameters of Promotions API.
 */
data class PromotionsResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("outputParameters")
    val outputParameters: OutputParameters1?,
    @SerializedName("simulationDetails")
    val simulationDetails: Map<String, Any?>? = mutableMapOf(),
    @SerializedName("status")
    val status: Boolean?,
)

data class OutputParameters1(
    @SerializedName("outputParameters")
    val outputParameters: OutputParameters2?
)

data class OutputParameters2(
    @SerializedName("results")
    val results: List<Results>
)

data class Results(
    @SerializedName("loyaltyPromotionType")
    val loyaltyPromotionType: String?,
    @SerializedName("maximumPromotionRewardValue")
    val maximumPromotionRewardValue: Int?,
    @SerializedName("totalPromotionRewardPointsVal")
    val totalPromotionRewardPointsVal: Int?,
    @SerializedName("loyaltyProgramCurrency")
    val loyaltyProgramCurrency: String?,
    @SerializedName("memberEligibilityCategory")
    val memberEligibilityCategory: String?,
    @SerializedName("fulfillmentAction")
    val fulfillmentAction: String?,
    @SerializedName("promotionName")
    val promotionName: String?,
    @SerializedName("promotionId")
    val promotionId: String?,
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("promEnrollmentStartDate")
    val promEnrollmentStartDate: String?,
    @SerializedName("promotionEnrollmentEndDate")
    val promotionEnrollmentEndDate: String?,
    @SerializedName("promotionImageUrl")
    val promotionImageUrl: String?,
    @SerializedName("promotionEnrollmentRqr")
    val promotionEnrollmentRqr: Boolean?
)