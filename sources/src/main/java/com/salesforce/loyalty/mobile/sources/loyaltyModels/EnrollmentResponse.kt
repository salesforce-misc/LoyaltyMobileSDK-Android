package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * EnrollmentResponse data class holds response parameters of User Enrollment API.
 */
data class EnrollmentResponse(
    @SerializedName("contactId")
    val contactId: String?,
    @SerializedName("loyaltyProgramMemberId")
    val loyaltyProgramMemberId: String?,
    @SerializedName("loyaltyProgramName")
    val loyaltyProgramName: String?,
    @SerializedName("membershipNumber")
    val membershipNumber: String?,
    @SerializedName("transactionJournals")
    val transactionJournals: List<TransactionJournals> = mutableListOf()
)

data class TransactionJournals(
    @SerializedName("activityDate")
    val activityDate: String?,
    @SerializedName("journalSubType")
    val journalSubType: String?,
    @SerializedName("journalType")
    val journalType: String?,
    @SerializedName("loyaltyProgram")
    val loyaltyProgram: String?,
    @SerializedName("loyaltyProgramMember")
    val loyaltyProgramMember: String?,
    @SerializedName("referredMember")
    val referredMember: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("transactionJournalId")
    val transactionJournalId: String?
)