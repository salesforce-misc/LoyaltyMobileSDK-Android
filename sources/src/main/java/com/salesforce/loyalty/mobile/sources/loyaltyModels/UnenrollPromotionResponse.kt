package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * UnenrollPromotionResponse data class holds response parameters of Unenroll Promotion API.
 */
data class UnenrollPromotionResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?
)