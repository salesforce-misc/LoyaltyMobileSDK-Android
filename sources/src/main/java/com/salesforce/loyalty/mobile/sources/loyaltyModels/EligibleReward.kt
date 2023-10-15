package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class EligibleReward(
    @SerializedName("Color")
    val seg_color: String,

    @SerializedName("Description")
    val description: String,

    @SerializedName("Image")
    val image: String,


    @SerializedName("Name")
    val name: String,

    @SerializedName("Reward Definition")
    val reward_definition: String,

    @SerializedName("Reward Type")
    val reward_type: String,

    @SerializedName("Reward Value")
    val reward_value: String
)