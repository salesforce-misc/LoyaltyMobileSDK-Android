package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

/**
 * Holds the Referral Program types i.e. whether member joined the program or yet to signup
 */
enum class ReferralProgramType(val status: String) {
    SIGNUP("SIGNUP"),
    JOIN_PROGRAM("JOIN_PROGRAM"),
    START_REFERRING("START_REFERRING")
}