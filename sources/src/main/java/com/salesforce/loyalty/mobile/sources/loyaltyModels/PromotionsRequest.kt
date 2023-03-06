package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * PromotionsRequest data class holds request parameters of Promotions API.
 */
data class PromotionsRequest(
    @SerializedName("processParameters")
    val processParameters: List<Map<String, Any?>?> = mutableListOf()
)