package com.salesforce.referral_sdk.entities

import com.google.gson.annotations.SerializedName

const val ATTRIBUTES_STATE = "State__c"
const val ATTRIBUTES_COUNTRY = "Country__c"

data class ReferralNewEnrollmentRequestBody(
    val additionalMemberFieldValues: ReferralAttributes?,
    val associatedPersonAccountDetails: AssociatedPersonAccountDetails,
    val enrollmentChannel: String,
    val memberStatus: String,
    val membershipNumber: String,
    val transactionJournalStatementFrequency: String,
    val transactionJournalStatementMethod: String
): BaseRequest()

data class AssociatedPersonAccountDetails(
    val additionalPersonAccountFieldValues: ReferralAttributes?,
    val allowDuplicateRecords: String,
    val email: String,
    val firstName: String,
    val lastName: String
)

data class ReferralAttributes(
    @SerializedName("attributes")
    val attributes: Map<String, Any?>? = mutableMapOf()
)

