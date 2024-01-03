package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName

data class ReferralCode(
    @SerializedName("MembershipNumber")
    val membershipNumber: String?,
    @SerializedName("ReferralCode")
    val referralCode: String?
)