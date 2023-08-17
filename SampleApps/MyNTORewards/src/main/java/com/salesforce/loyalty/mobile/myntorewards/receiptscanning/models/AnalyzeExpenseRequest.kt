package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class AnalyzeExpenseRequest(
    @SerializedName("base64image")
    val base64image: String
)
data class ReceiptListResponse(
    val done: Boolean,
    val records: List<Record>,
    val totalSize: Int
)

data class Record(
    val Id: String,
    val Name: String,
    val Purchase_Date__c: String,
    val ReceiptId__c: String,
    val Status__c: String,
    val StoreName__c: String,
    val TotalAmount__c: Double,
    val Total_Points__c: Any,
    val attributes: Attributes
)

data class Attributes(
    val type: String,
    val url: String
)