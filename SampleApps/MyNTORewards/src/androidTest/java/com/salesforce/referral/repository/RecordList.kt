package com.salesforce.referral.repository

import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity

data class RecordList(
    val records: List<ReferralEntity>? = mutableListOf()
)