package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName

data class ReferralEnrollmentInfo(
    @SerializedName("LoyaltyProgramMemberId")
    val loyaltyProgramMemberId: String?,
    @SerializedName("LoyaltyProgramMember")
    val loyaltyProgramMember: ReferralContactInfo?,
)

data class ReferralContactInfo(
    @SerializedName("ContactId")
    val contactId: String?
)