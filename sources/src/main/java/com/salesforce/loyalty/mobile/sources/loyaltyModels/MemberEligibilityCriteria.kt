package com.salesforce.loyalty.mobile.sources.loyaltyModels

enum class MemberEligibilityCriteria(val criteria: String) {
    ELIGIBLE("Eligible"),
    ELIGIBLE_BUT_NOT_ENROLLED("EligibleButNotEnrolled"),
    INELIGIBLE("Ineligible")
}