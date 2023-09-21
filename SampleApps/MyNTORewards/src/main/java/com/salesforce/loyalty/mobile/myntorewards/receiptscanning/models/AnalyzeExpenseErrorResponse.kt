package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class AnalyzeExpenseErrorResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("errorCode")
    val errorCode: String?,
)
