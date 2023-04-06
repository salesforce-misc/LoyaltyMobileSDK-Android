package com.salesforce.loyalty.mobile.myntorewards.checkout.models

import com.google.gson.annotations.SerializedName

data class ShippingMethodsResponse(
    val transactionJournals: List<ShippingMethod> = mutableListOf()
)

data class ShippingMethod(
    @SerializedName("attributes")
    val attributes: Attributes?,
    @SerializedName("Shipping_Code__c")
    val shppingCode: String?,
    @SerializedName("Shipping_Type__c")
    val shippingType: String?,
    @SerializedName("Id")
    val id: String?
)

data class Attributes(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)