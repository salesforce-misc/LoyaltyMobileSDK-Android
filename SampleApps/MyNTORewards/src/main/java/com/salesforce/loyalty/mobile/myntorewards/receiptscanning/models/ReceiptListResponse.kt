package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class ReceiptListResponse(
    val done: Boolean,
    val records: List<Record>,
    val totalSize: Int
)

data class Record(
    @SerializedName("Id")
    val id: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Purchase_Date__c")
    val purchase_date: String,

    @SerializedName("ReceiptId" +
            "__c")
    val receipt_id: String,

    @SerializedName("Status__c")
    val receipt_status: String,

    @SerializedName("StoreName__c")
    val store_name: String,

    @SerializedName("TotalAmount__c")
    val total_amount: String?,

    @SerializedName("Processed_AWS_Response__c")
    val processedAWSResponse: String?,

    @SerializedName("Total_Points__c")
    val total_points: Any,

    @SerializedName("attributes")
    val attributes: Attributes
)

data class Attributes(
    val type: String,
    val url: String
)