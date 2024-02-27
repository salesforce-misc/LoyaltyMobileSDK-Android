package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class CreateTransactionalJournalResponse(
    @SerializedName("attributes")
    val attributes: TransactionAttributes?,
    @SerializedName("Id")
    val transactionId: String?
)

data class TransactionAttributes(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)