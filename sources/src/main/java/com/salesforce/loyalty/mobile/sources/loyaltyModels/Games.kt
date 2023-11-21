package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class Games(

    @SerializedName("errorMessage")
    val errorMessage: String?,

    @SerializedName("gameDefinitions")
    val gameDefinitions: List<GameDefinition>,

    @SerializedName("status")
    val status: Boolean
)