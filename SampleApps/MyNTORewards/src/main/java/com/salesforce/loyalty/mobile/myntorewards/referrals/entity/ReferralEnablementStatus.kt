package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName

data class ReferralEnablementStatus(
    @SerializedName("QualifiedApiName")
    val qualifiedApiName: String?
)