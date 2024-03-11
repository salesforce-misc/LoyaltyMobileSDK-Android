package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

/**
 * Holds the Referral Program types i.e. whether member joined the program or yet to signup
 */
sealed class ReferralProgramType {
    object SIGNUP : ReferralProgramType()
    object JOIN_PROGRAM : ReferralProgramType()
    object START_REFERRING : ReferralProgramType()
    object EMPTY_STATE : ReferralProgramType()
    data class ERROR_ENROLL(val errorMessage: String? = null) : ReferralProgramType()
    data class ERROR_REFERRAL_EVENT(val errorMessage: String? = null) : ReferralProgramType()
    data class ERROR_PROMOTION_EXPIRED(val errorMessage: String? = null) : ReferralProgramType()
}