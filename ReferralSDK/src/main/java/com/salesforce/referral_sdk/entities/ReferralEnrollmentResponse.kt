package com.salesforce.referral_sdk.entities

data class ReferralEnrollmentResponse(
    val contactId: String,
    val memberId: String,
    val membershipNumber: String,
    val programName: String,
    val promotionReferralCode: String,
    val transactionJournals: List<TransactionJournal>
)

data class TransactionJournal(
    val activityDate: String,
    val journalSubType: String,
    val journalType: String,
    val membershipNumber: String,
    val programName: String,
    val status: String,
    val transactionJournalId: String
)