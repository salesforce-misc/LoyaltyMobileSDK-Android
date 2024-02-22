package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName

data class ReferralPromotionStatusAndPromoCode(
    @SerializedName("LoyaltyProgramMemberId")
    val loyaltyProgramMemberId: String?,
    @SerializedName("LoyaltyProgramMember")
    val loyaltyProgramMember: ReferralMemberContactInfo?,
    @SerializedName("Promotion")
    val promotion: ReferralPromotion?,
    @SerializedName("PromotionCode")
    val promotionCode: String?,
    @SerializedName("IsReferralPromotion")
    val isReferralPromotion: Boolean
)

data class ReferralMemberContactInfo(
    @SerializedName("ContactId")
    val contactId: String?
)

data class ReferralPromotion(
    @SerializedName("PromotionCode")
    val promotionCode: String?,
    @SerializedName("IsReferralPromotion")
    val isReferralPromotion: Boolean
)