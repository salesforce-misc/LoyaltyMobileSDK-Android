package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class GameDefinition(

    @SerializedName("description")
    val description: String?,

    @SerializedName("gameDefinitionId")
    val gameDefinitionId: String?,

    @SerializedName("endDate")
    val endDate: String?,

    @SerializedName("gameRewards")
    val gameRewards: List<GameReward> = mutableListOf(),

    @SerializedName("participantGameRewards")
    val participantGameRewards: List<ParticipantGameReward> = mutableListOf(),

    @SerializedName("name")
    val name: String?,

    @SerializedName("startDate")
    val startDate: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("timeoutDuration")
    val timeoutDuration: Int?,

    @SerializedName("type")
    val type: String
)