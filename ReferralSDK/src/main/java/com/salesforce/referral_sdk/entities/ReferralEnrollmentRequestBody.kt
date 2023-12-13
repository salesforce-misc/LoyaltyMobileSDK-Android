package com.salesforce.referral_sdk.entities

import com.google.gson.annotations.SerializedName

data class ReferralEnrollmentRequestBody(
    @SerializedName("membershipNumber")
    val membershipNumber: String?
)
