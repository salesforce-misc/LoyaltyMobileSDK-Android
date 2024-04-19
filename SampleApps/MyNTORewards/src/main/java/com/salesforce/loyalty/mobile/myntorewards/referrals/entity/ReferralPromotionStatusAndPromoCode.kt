package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName

data class ReferralPromotionStatusAndPromoCode(
    @SerializedName("Name")
    val name: String?,
    @SerializedName("PromotionCode")
    val promotionCode: String?,
    @SerializedName("PromotionPageUrl")
    val promotionPageUrl: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("EndDate")
    val endDate: String?,
    @SerializedName("ImageUrl")
    val imageUrl: String?,
    @SerializedName("IsReferralPromotion")
    val isReferralPromotion: Boolean
)