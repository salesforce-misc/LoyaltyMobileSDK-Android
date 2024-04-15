package com.salesforce.loyalty.mobile.myntorewards.badge.models

data class LoyaltyBadgeListProgramMember(
    val done: Boolean,
    val records: List<LoyaltyProgramMemberBadgeListRecord>,
    val totalSize: Int
)
