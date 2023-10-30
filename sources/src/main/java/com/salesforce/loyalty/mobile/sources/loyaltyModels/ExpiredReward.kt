package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class ExpiredReward(
    @SerializedName("Description")
    val description: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Reward Type")
    val reward_type: String,

)