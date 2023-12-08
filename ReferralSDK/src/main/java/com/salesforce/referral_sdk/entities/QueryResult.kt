package com.salesforce.referral_sdk.entities

import com.google.gson.annotations.SerializedName

data class QueryResult<T>(
    @SerializedName("totalSize")
    val totalSize: Int?,
    @SerializedName("done")
    val isDone: Boolean?,
    @SerializedName("records")
    val records: List<T>? = mutableListOf(),
    @SerializedName("nextRecordsUrl")
    val nextRecordsUrl: String?
)