package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class GameRewardResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("GameRewards")
    val gameRewards: List<GameRewards> = mutableListOf()
)

data class GameRewards(
    @SerializedName("Name")
    val name: String?,

    @SerializedName("gameRewardId")
    val gameRewardId: String?,

    @SerializedName("Description")
    val description: String?,
    @SerializedName("Reward Type")
    val rewardType: String?,
    @SerializedName("Reward Definition")
    val rewardDefinition: String?,
    @SerializedName("Reward Value")
    val rewardValue: String?,
    @SerializedName("Status")
    val status: String?,
    @SerializedName("Expiration Date")
    val expirationDate: String?,
    @SerializedName("Issued Reward Reference")
    val issuedRewardReference: String?
)