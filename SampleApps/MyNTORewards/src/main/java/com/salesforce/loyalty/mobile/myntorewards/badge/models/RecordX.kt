package com.salesforce.loyalty.mobile.myntorewards.badge.models

data class RecordX(
    val Description: String,
    val ImageUrl: String,
    val LoyaltyProgramId: String,
    val Name: String,
    val StartDate: String,
    val Status: String,
    val ValidityDuration: Int,
    val ValidityDurationUnit: String,
    val ValidityEndDate: String,
    val ValidityType: String,
    val attributes: AttributesX
)