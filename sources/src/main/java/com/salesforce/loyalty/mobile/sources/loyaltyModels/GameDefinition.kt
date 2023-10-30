package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class GameDefinition(
    @SerializedName("Description")
    val description: String,

    @SerializedName("End Date")
    val end_date: String,

    @SerializedName("GameReward")
    val gameReward: GameReward,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Start Date")
    val start_date: String,

    @SerializedName("Status")
    val status: String,

    @SerializedName("Timeout Duration")
    val timeout_duration: String,

    @SerializedName("Type")
    val type: String
)