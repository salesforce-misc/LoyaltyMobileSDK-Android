package com.salesforce.loyalty.mobile.myntorewards.badge.models

data class LoyaltyBadgeList(
    val done: Boolean,
    val records: List<Record>,
    val totalSize: Int
)