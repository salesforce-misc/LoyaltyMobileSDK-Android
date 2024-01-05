package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class AnalyzeExpenseResponse(
    @SerializedName("memberShipNumber")
    val membershipNumber: String,
    @SerializedName("totalAmount")
    val totalAmount: String?,
    @SerializedName("storeName")
    val storeName: String?,
    @SerializedName("storeAddress")
    val storeAddress: String?,
    @SerializedName("receiptNumber")
    val receiptNumber: String?,
    @SerializedName("receiptSFDCId")
    val receiptId: String?,
    @SerializedName("receiptDate")
    val receiptDate: String?,
    @SerializedName("lineItem")
    val lineItems: List<LineItem> = mutableListOf(),
    @SerializedName("dateFormat")
    val dateFormat: String?,
    @SerializedName("confidenceStatus")
    val confidenceStatus: String?,
)

data class LineItem(
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("lineItemPrice")
    val lineItemPrice: String?,
    @SerializedName("isEligible")
    val isEligible: Boolean?
)