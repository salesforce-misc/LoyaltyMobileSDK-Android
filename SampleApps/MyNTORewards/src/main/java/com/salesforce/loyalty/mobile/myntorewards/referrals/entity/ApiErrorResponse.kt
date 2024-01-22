package com.salesforce.loyalty.mobile.myntorewards.referrals.entity

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("errorCode")
    val errorCode: String?,
)