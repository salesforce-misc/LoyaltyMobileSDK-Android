package com.salesforce.loyalty.mobile.myntorewards.badge.models

data class LoyaltyBadgeDetails(
    val done: Boolean,
    val records: List<RecordX>,
    val totalSize: Int
)