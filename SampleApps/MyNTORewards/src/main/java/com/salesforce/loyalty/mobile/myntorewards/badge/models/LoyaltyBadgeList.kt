package com.salesforce.loyalty.mobile.myntorewards.badge.models

data class LoyaltyBadgeList<T>(
    val done: Boolean,
    val records: List<T>,
    val totalSize: Int
)