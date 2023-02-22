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
    @SerializedName("totalCount")
    val totalCount: Int?,
    @SerializedName("transactionJournals")
    val transactionJournals: List<TransactionsJournals> = mutableListOf()
)

data class TransactionsJournals (
    @SerializedName("Id")
    val id: String?,
    @SerializedName("JournalNumber")
    val journalNumber: String?,
    @SerializedName("ActivityDate")
    val activityDate: String?,
    @SerializedName("JournalType")
    val journalType: String?,
    @SerializedName("JournalSubType")
    val journalSubType: String?,
    @SerializedName("TransactionAmount")
    val transactionAmount: Int?,
    @SerializedName("productCategory")
    val productCategory: String?,
    @SerializedName("product")
    val product: String?,
    @SerializedName("ExternalTransactionNumber")
    val externalTransactionNumber: String?,
    @SerializedName("loyaltyProgramCurrencyDetails")
    val loyaltyProgramCurrencyDetails: List<LoyaltyProgramCurrencyDetails> = mutableListOf(),
    @SerializedName("additionalAttributes")
    val additionalAttributes: List<AdditionalAttributes> = mutableListOf()
)

data class AdditionalAttributes (
    @SerializedName("value" )
    val value : String?,
    @SerializedName("key")
    val key   : String?
)

data class LoyaltyProgramCurrencyDetails (
    @SerializedName("value")
    val value : Int?,
    @SerializedName("name")
    val name  : String?
)