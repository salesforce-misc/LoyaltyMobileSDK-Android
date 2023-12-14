package com.salesforce.referral_sdk.entities


data class ReferralEnrollmentRequestBody(
    val additionalMemberFieldValues: AdditionalMemberFieldValues,
    val associatedPersonAccountDetails: AssociatedPersonAccountDetails,
    val enrollmentChannel: String,
    val memberStatus: String,
    val membershipNumber: String,
    val transactionJournalStatementFrequency: String,
    val transactionJournalStatementMethod: String
)

data class AdditionalMemberFieldValues(
    val attributes: ReferralAttributes
)

data class AssociatedPersonAccountDetails(
    val additionalPersonAccountFieldValues: AdditionalPersonAccountFieldValues,
    val allowDuplicateRecords: String,
    val email: String,
    val firstName: String,
    val lastName: String
)

data class ReferralAttributes(
    val state_c: String
)

data class AdditionalPersonAccountFieldValues(
    val attributes: AttributesX
)

data class AttributesX(
    val country_c: String
)
