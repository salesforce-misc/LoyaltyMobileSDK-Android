package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

import com.google.gson.annotations.SerializedName

data class AnalyzeExpenseRequest(
    @SerializedName("base64image")
    val base64image: String
)