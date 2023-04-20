package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import com.google.gson.annotations.SerializedName

data class ForceAuth(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("instance_url")
    val instanceURL: String,
    @SerializedName("id")
    val identityURL: String,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("issued_at")
    val timestamp: String?,
    @SerializedName("signature")
    val signature: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?
)