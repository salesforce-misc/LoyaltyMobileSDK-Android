/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

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
    @SerializedName("externalTransactionNumber" )
    val externalTransactionNumber: String?,
    @SerializedName("transactionJournals")
    val transactionJournals: List<TransactionsJournals> = mutableListOf()
)

data class TransactionsJournals (
    @SerializedName("activityDate")
    val activityDate: String?,
    @SerializedName("additionalTransactionJournalAttributes")
    val additionalTransactionJournalAttributes: List<AdditionalAttributes> = mutableListOf(),
    @SerializedName("journalTypeName")
    val journalTypeName: String?,
    @SerializedName("journalSubTypeName")
    val journalSubTypeName: String?,
    @SerializedName("transactionAmount")
    val transactionAmount: String?,
    @SerializedName("pointsChange")
    val pointsChange: List<PointsChange> = mutableListOf(),
    @SerializedName("transactionJournalId")
    val transactionJournalId: String?,
    @SerializedName("transactionJournalNumber")
    val transactionJournalNumber: String?

)

data class PointsChange (
    @SerializedName("changeInPoints")
    val changeInPoints: Double?,
    @SerializedName("loyaltyMemberCurrency")
    val loyaltyMemberCurrency: String?
)

data class AdditionalAttributes(
    @SerializedName("fieldName")
    val fieldName: String?,
    @SerializedName("value")
    val value: String?
)