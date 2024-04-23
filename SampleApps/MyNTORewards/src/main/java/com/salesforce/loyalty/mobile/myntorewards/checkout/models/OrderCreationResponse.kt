package com.salesforce.loyalty.mobile.myntorewards.checkout.models

import com.google.gson.annotations.SerializedName

data class OrderCreationResponse(
    @SerializedName("orderId")
    val orderId: String?,
    @SerializedName("gameParticipantRewardId")
    val gameParticipantRewardId: String?,
)