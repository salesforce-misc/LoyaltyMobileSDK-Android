package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class ParticipantGameReward(
    @SerializedName("gameRewardId")
    val gameRewardId: String?,

    @SerializedName("gameParticipantRewardId")
    val gameParticipantRewardId: String?,

    @SerializedName("sourceActivityId")
    val sourceActivityId: String?,

    @SerializedName("expirationDate")
    val expirationDate: String?,

    @SerializedName("issuedRewardReference")
    val issuedRewardReference: String?,

    @SerializedName("status")
    val status: String?,
)