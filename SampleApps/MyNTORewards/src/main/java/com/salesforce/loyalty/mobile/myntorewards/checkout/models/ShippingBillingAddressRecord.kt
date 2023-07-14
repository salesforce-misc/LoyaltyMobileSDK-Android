package com.salesforce.loyalty.mobile.myntorewards.checkout.models

import com.google.gson.annotations.SerializedName

data class ShippingBillingAddressRecord(
    @SerializedName("attributes")
    val attributes: RecordAttributes?,
    @SerializedName("ShippingAddress")
    val shippingAddress: Address?,
    @SerializedName("BillingAddress")
    val billingAddress: Address?
)

data class RecordAttributes(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)

data class Address(
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("geocodeAccuracy")
    val geocodeAccuracy: String?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("postalCode")
    val postalCode: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("street")
    val street: String?
)