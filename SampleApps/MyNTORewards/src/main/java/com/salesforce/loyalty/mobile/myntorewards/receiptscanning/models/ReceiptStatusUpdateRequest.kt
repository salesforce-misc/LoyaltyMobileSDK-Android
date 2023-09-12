package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class ReceiptStatusUpdateRequest(
    @SerializedName("receiptId")
    val receiptId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("comments")
    val comments: String?,
)