package com.salesforce.referral_sdk.entities

import com.salesforce.referral_sdk.MemberStatus


data class ReferralExistingEnrollmentRequest(
    val membershipNumber: String? = null,
    val contactId: String? = null,
    val memberStatus: String = MemberStatus.ACTIVE.status
): BaseRequest()