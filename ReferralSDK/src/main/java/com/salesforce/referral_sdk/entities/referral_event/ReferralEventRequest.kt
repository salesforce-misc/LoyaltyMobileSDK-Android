package com.salesforce.referral_sdk.entities.referral_event

import com.google.gson.annotations.SerializedName

data class ReferralEventRequest(
    @SerializedName("referralCode")
    val referralCode: String,
    @SerializedName("joiningDate")
    val joiningDate: String,
    @SerializedName("eventType")
    val eventType: String,
    @SerializedName("referralEmails")
    val referralEmails: Emails
)

data class Emails(
    @SerializedName("emails")
    val emails: List<String>,
)