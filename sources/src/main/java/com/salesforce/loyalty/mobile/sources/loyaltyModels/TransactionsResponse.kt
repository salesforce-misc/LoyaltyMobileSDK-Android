package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * Transactions data class holds response parameters of Transactions API.
 */
data class TransactionsResponse (
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("totalCount")
    var totalCount: Int?,
    @SerializedName("transactionJournals")
    var transactionJournals: List<TransactionsJournals> = mutableListOf()
)

data class TransactionsJournals (
    @SerializedName("Id")
    val id: String?,
    @SerializedName("JournalNumber")
    var journalNumber: String?,
    @SerializedName("ActivityDate")
    val activityDate: String?,
    @SerializedName("JournalType")
    var journalType: String?,
    @SerializedName("JournalSubType")
    var journalSubType: String?,
    @SerializedName("TransactionAmount")
    var transactionAmount: Int?,
    @SerializedName("productCategory")
    var productCategory: String?,
    @SerializedName("product")
    var product: String?,
    @SerializedName("ExternalTransactionNumber")
    var externalTransactionNumber: String?,
    @SerializedName("loyaltyProgramCurrencyDetails")
    var loyaltyProgramCurrencyDetails: List<LoyaltyProgramCurrencyDetails> = mutableListOf(),
    @SerializedName("additionalAttributes")
    var additionalAttributes: List<AdditionalAttributes> = mutableListOf()
)

data class AdditionalAttributes (
    @SerializedName("value" )
    var value : String?,
    @SerializedName("key")
    var key   : String?
)

data class LoyaltyProgramCurrencyDetails (
    @SerializedName("value")
    var value : Int?,
    @SerializedName("name")
    var name  : String?
)