package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig.RECEIPT_NAMESPACE

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

    @SerializedName("${RECEIPT_NAMESPACE}__PurchaseDate__c")
    val purchase_date: String?,

    @SerializedName("${RECEIPT_NAMESPACE}__ReceiptID__c")
    val receipt_id: String,

    @SerializedName("${RECEIPT_NAMESPACE}__Status__c")
    val receipt_status: String,

    @SerializedName("${RECEIPT_NAMESPACE}__StoreName__c")
    val store_name: String,

    @SerializedName("${RECEIPT_NAMESPACE}__TotalAmount__c")
    val total_amount: String?,

    @SerializedName("${RECEIPT_NAMESPACE}__ImageUrl__c")
    val imageUrl: String?,

    @SerializedName("${RECEIPT_NAMESPACE}__APIResponse__c")
    val processedAWSResponse: String?,

    @SerializedName("${RECEIPT_NAMESPACE}__TotalRewardPoints__c")
    val total_points: Double?,

    @SerializedName("attributes")
    val attributes: Attributes
)

data class Attributes(
    val type: String,
    val url: String
)