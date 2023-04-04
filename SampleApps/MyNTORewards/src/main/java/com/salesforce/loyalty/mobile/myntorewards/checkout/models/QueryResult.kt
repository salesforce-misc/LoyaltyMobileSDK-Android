package com.salesforce.loyalty.mobile.myntorewards.checkout.models

import com.google.gson.annotations.SerializedName

data class QueryResult<T>(
    @SerializedName("totalSize")
    val totalSize: Int?,
    @SerializedName("isDone")
    val done: Boolean?,
    @SerializedName("records")
    val records: List<T>? = mutableListOf(),
    @SerializedName("nextRecordsUrl")
    val nextRecordsUrl: String?
)