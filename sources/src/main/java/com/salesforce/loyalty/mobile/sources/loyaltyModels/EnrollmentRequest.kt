package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * EnrollmentRequest data class holds request parameters of User Enrollment API.
 */
data class EnrollmentRequest(
    @SerializedName("enrollmentDate")
    val enrollmentDate: String?,
    @SerializedName("membershipNumber")
    val membershipNumber: String,
    @SerializedName("associatedContactDetails")
    val associatedContactDetails: AssociatedContactDetails,
    @SerializedName("memberStatus")
    val memberStatus: String?,
    @SerializedName("createTransactionJournals")
    val createTransactionJournals: Boolean,
    @SerializedName("transactionJournalStatementFrequency")
    val transactionJournalStatementFrequency: String?,
    @SerializedName("transactionJournalStatementMethod")
    val transactionJournalStatementMethod: String?,
    @SerializedName("enrollmentChannel")
    val enrollmentChannel: String?,
    @SerializedName("canReceivePromotions")
    val canReceivePromotions: Boolean,
    @SerializedName("canReceivePartnerPromotions")
    val canReceivePartnerPromotions: Boolean,
    @SerializedName("membershipEndDate")
    val membershipEndDate: String?,
    @SerializedName("additionalMemberFieldValues")
    val additionalMemberFieldValues: AdditionalMemberFieldValues
)

data class AssociatedContactDetails(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("allowDuplicateRecords")
    val allowDuplicateRecords: Boolean,
    @SerializedName("additionalContactFieldValues")
    val additionalContactFieldValues: AdditionalContactFieldValues
)

data class AdditionalContactFieldValues(
    @SerializedName("attributes")
    val attributes: Map<String, Any?>? = mutableMapOf()
)

data class AdditionalMemberFieldValues(
    @SerializedName("attributes")
    val attributes: Map<String, Any?>? = mutableMapOf()
)
