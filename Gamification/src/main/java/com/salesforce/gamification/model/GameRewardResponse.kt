package com.salesforce.gamification.model

import com.google.gson.annotations.SerializedName

data class GameRewardResponse(
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("gameReward")
    val gameRewards: List<GameRewards> = mutableListOf()
)

data class GameRewards(
    @SerializedName("rewardDefinitionId")
    val rewardDefinitionId: String?,

    @SerializedName("color")
    val color: String?,

    @SerializedName("description")
    val description: String?,
    @SerializedName("expirationDate")
    val expirationDate: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("issuedRewardReference")
    val issuedRewardReference: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("gameRewardId")
    val gameRewardId: String?,
    @SerializedName("rewardType")
    val rewardType: String?,
    @SerializedName("rewardValue")
    val rewardValue: String?,
)
