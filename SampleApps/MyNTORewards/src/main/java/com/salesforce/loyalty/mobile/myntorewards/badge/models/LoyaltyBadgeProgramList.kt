package com.salesforce.loyalty.mobile.myntorewards.badge.models

data class LoyaltyBadgeProgramList(
    val done: Boolean,
    val records: List<LoyaltyProgramBadgeListRecord>,
    val totalSize: Int
)
