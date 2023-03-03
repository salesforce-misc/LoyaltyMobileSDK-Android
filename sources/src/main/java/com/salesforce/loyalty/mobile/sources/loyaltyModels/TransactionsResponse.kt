package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * Transactions data class holds response parameters of Transactions API.
 */
data class TransactionsResponse (
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("transactionJournalCount" )
    val transactionJournalCount: Int?,
    @SerializedName("transactionJournals")
    val transactionJournals: List<TransactionsJournals> = mutableListOf()
)

data class TransactionsJournals (
    @SerializedName("activityDate")
    val activityDate: String?,
    @SerializedName("journalTypeName")
    val journalTypeName: String?,
    @SerializedName("pointsChange")
    val pointsChange: List<PointsChange> = mutableListOf(),
    @SerializedName("transactionJournalId")
    val transactionJournalId: String?

)

data class PointsChange (
    @SerializedName("name")
    val name: String?,
    @SerializedName("value")
    val value: String?
)