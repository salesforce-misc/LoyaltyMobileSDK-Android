package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

data class ConnectedApp(
    val name: String,
    val consumerKey: String,
    val consumerSecret: String,
    val callbackUrl: String,
    val baseUrl: String,
    val instanceUrl: String,
    val communityUrl: String
)