package com.salesforce.referral_sdk.entities.referral_event

import com.google.gson.annotations.SerializedName

data class ReferralEventRequest(
    @SerializedName("referralCode")
    val referralCode: String,
    @SerializedName("joiningDate")
    val joiningDate: String,
    @SerializedName("eventType")
    val eventType: String = ReferralEventType.REFER.eventType,
    @SerializedName("email")
    val email: String?,
    @SerializedName("referralEmails")
    val referralEmails: String?,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("contactId")
    val contactId: String? = null,
    @SerializedName("additionalAttributes")
    val additionalAttributes: AdditionalAttributes? = null,
    @SerializedName("orderReferenceId")
    val orderReferenceId: String? = null,
    @SerializedName("productId")
    val productId: String? = null,
    @SerializedName("purchaseAmount")
    val purchaseAmount: Int? = null,
    @SerializedName("purchaseQuantity")
    val purchaseQuantity: Int? = null,
    @SerializedName("transactionJournalAdditionalAttributes")
    val transactionJournalAdditionalAttributes: TransactionJournalAdditionalAttributes? = null
)