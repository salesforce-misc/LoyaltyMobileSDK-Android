package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class AnalyzeExpenseResponse(
    @SerializedName("totalAmount")
    val totalAmount: String?,
    @SerializedName("storeName")
    val storeName: String?,
    @SerializedName("storeAddress")
    val storeAddress: String?,
    @SerializedName("receiptNumber")
    val receiptNumber: String?,
    @SerializedName("receiptDate")
    val receiptDate: String?,
    @SerializedName("lineItem")
    val lineItems: List<LineItem> = mutableListOf()
)

data class LineItem(
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("lineItemPrice")
    val lineItemPrice: Double?
)