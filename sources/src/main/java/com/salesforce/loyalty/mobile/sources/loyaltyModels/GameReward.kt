package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class GameReward(
    @SerializedName("color")
    val segColor: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("expirationDate")
    val expirationDate: String?,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("rewardDefinitionId")
    val rewardDefinitionId: String?,

    @SerializedName("rewardType")
    val rewardType: String?,

    @SerializedName("rewardValue")
    val rewardValue: String?,

    @SerializedName("gameRewardId")
    val gameRewardId: String,
)