package com.salesforce.loyalty.mobile.myntorewards.checkout.models

import com.google.gson.annotations.SerializedName

data class OrderCreationRequest(
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("redeemPoints")
    val redeemPoints: Int?,
    @SerializedName("productPrice")
    val productPrice: Int?,
    @SerializedName("orderTotal")
    val orderTotal: Int?,
    @SerializedName("useNTOPoints")
    val useNTOPoints: Boolean,
    @SerializedName("pointsBalance")
    val pointsBalance: Int?,
    @SerializedName("shippingStreet")
    val shippingStreet: String?,
    @SerializedName("billingStreet")
    val billingStreet: String?,
    @SerializedName("voucherCode")
    val voucherCode: String?,
    @SerializedName("membershipNumber")
    val membershipNumber: String?,
)