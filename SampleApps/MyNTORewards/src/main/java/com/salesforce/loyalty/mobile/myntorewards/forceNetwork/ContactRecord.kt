package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import com.google.gson.annotations.SerializedName

data class ContactRecord(
    @SerializedName("attributes")
    val attributes: Attributes1?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("Phone")
    val phone: String?
)

data class Attributes1(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)