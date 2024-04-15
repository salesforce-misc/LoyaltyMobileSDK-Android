package com.salesforce.loyalty.mobile.myntorewards.badge.models

import com.google.gson.annotations.SerializedName

data class LoyaltyProgramMemberBadgeListRecord(

    @SerializedName("EndDate")
    val endDate: String,

    @SerializedName("LoyaltyProgramBadgeId")
    val loyaltyProgramBadgeId: String,

    @SerializedName("Description")
    val description: String,

    @SerializedName("LoyaltyProgramMemberId")
    val loyaltyProgramMemberId: String,

    @SerializedName("Name")
    val name: String,


    @SerializedName("Reason")
    val reason: String,

    @SerializedName("StartDate")
    val startDate: String,

    @SerializedName("Status")
    val status: String,

    @SerializedName("attributes")
    val attributes: Attributes
)