package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class CreateTransactionJournalRequest(
    @SerializedName("membershipNumber")
    val membershipNumber: String,
    @SerializedName("base64image")
    val base64image: String
)