package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class GameReward(
    @SerializedName("AssignedRewards")
    val assignedRewards: List<AssignedReward>,

    @SerializedName("EligibleRewards")
    val eligibleRewards: List<EligibleReward>,

    @SerializedName("ExpiredRewards")
    val expiredRewards: List<ExpiredReward>
)