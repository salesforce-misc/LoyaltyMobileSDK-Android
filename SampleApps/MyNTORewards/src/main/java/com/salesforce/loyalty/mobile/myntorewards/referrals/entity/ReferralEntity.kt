package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

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
    val promotionStage: CurrentPromotionStage?,
    @SerializedName("ReferredParty")
    val referredParty: ReferredParty?
)

data class CurrentPromotionStage(
    @SerializedName("Type")
    val type: String?
)

data class ReferredParty(
    @SerializedName("Account")
    val account: ReferredAccount?
)

data class ReferredAccount(
    @SerializedName("PersonEmail")
    val personEmail: String?
)

data class Attributes(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)