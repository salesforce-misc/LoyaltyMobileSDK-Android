package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

/**
 * Holds the Referral Program types i.e. whether member joined the program or yet to signup
 */
sealed class ReferralProgramType {
    object SIGNUP : ReferralProgramType()
    object JOIN_PROGRAM : ReferralProgramType()
    object START_REFERRING : ReferralProgramType()
    data class ERROR(val errorMessage: String? = null) : ReferralProgramType()
}