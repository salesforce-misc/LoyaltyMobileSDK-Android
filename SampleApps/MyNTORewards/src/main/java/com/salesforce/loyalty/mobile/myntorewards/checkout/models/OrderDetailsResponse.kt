package com.salesforce.loyalty.mobile.myntorewards.checkout.models

import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    @SerializedName("attributes")
    val attributes: OrderAttributes?,
    @SerializedName("Id")
    val id: String?,
    @SerializedName("OrderNumber")
    val orderNumber: String?,
    @SerializedName("ActivatedDate")
    val activatedDate: String?,

    )

data class OrderAttributes(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)