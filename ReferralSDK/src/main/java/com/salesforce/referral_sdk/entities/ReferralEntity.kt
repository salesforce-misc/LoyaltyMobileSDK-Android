package com.salesforce.referral_sdk.entities

import com.google.gson.annotations.SerializedName

data class ReferralEntity(
    @SerializedName("attributes")
    val attributes: Attributes?,
    @SerializedName("ClientEmail")
    val clientEmail: String?,
    @SerializedName("ReferrerEmail")
    val referrerEmail: String?,
    @SerializedName("ReferralDate")
    val referralDate: String?,
    @SerializedName("CurrentPromotionStage")
    val promotionStage: CurrentPromotionStage?
)

data class CurrentPromotionStage(
    @SerializedName("Type")
    val type: String?,
)

data class Attributes(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)