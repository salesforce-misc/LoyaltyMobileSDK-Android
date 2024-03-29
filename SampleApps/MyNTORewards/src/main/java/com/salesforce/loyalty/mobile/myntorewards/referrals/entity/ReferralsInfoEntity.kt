package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName
data class ReferralsInfoEntity(
    @SerializedName("referralList")
    val referralList: List<ReferralEntity>?,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("errorCode")
    val errorCode: String? = null
)