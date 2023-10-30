package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class Games(

    @SerializedName("GameDefinitions")
    val gameDefinitions: List<GameDefinition>,

    @SerializedName("message")
    val message: Any,

    @SerializedName("status")
    val status: Boolean
)