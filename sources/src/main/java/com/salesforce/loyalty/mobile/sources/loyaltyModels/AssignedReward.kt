package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class AssignedReward(
    @SerializedName("Color")
    val seg_color: String,

    @SerializedName("Description")
    val description: String,

    @SerializedName("Expiration Date")
    val expiration_date: String,

    @SerializedName("Image")
    val image: String,

    @SerializedName("Issued Reward Reference")
    val issued_reward_reference: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Reward Definition")
    val reward_definition: String,

    @SerializedName("Reward Type")
    val reward_type: String,

    @SerializedName("Reward Value")
    val reward_value: String
)