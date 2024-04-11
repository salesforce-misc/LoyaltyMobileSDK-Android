package com.salesforce.loyalty.mobile.myntorewards.badge.models

import com.google.gson.annotations.SerializedName

data class LoyaltyProgramBadgeListRecord(
    @SerializedName("Description")
    val description: String,

    @SerializedName("ImageUrl")
    val imageUrl: String,

    @SerializedName("LoyaltyProgramId")
    val loyaltyProgramId: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("StartDate")
    val startDate: String,

    @SerializedName("Status")
    val status: String,

    @SerializedName("Id")
    val id: String,

    @SerializedName("ValidityDuration")
    val validityDuration: Int,

    @SerializedName("ValidityDurationUnit")
    val validityDurationUnit: String,

    @SerializedName("ValidityEndDate")
    val validityEndDate: String,

    @SerializedName("ValidityType")
    val validityType: String,

    @SerializedName("attributes")
    val attributes: Attributes
)