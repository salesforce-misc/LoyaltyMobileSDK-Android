package com.salesforce.loyalty.mobile.myntorewards.badge.models

data class Record(
    val EndDate: String,
    val LoyaltyProgramBadgeId: String,
    val LoyaltyProgramMemberId: String,
    val Name: String,
    val Reason: String,
    val StartDate: String,
    val Status: String,
    val attributes: Attributes
)