package com.salesforce.referral_sdk.entities


data class ReferralExistingEnrollmentRequest(
    val membershipNumber: String? = null,
    val contactId: String? = null
): BaseRequest()