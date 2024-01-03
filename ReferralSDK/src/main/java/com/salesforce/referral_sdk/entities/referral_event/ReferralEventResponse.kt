package com.salesforce.referral_sdk.entities.referral_event

import com.google.gson.annotations.SerializedName

data class ReferralEventResponse(
    @SerializedName("contactId")
    val contactId: String,
    @SerializedName("referralId")
    val referralId: String,
    @SerializedName("referralStage")
    val referralStage: String,
    @SerializedName("transactionjournalIds")
    val transactionJournalIds: List<String>,
    @SerializedName("voucherId")
    val voucherId: String
)