package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName

data class ReferralEntity(
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