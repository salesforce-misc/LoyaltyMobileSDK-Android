package com.salesforce.loyalty.mobile.myntorewards.referrals

/**
 * Referral configuration class
 */
object ReferralConfig {
    // Referral Program Name
    const val REFERRAL_PROGRAM_NAME = "NTO Insider"
    // Configure Referral Promotion Details below, where user can also able to enroll and refer from My Referrals screen
    const val REFERRAL_DEFAULT_PROMOTION_CODE = "NEWPR2"
    const val REFERRAL_DEFAULT_PROMOTION_ID = "0c81Q0000004ZJtQAM"
    // Referral duration - By default, showing the last 90days of referrals info
    const val REFERRAL_DURATION = 90
    /* Provided random link here instead of actual Terms and Conditions link.
    Replace with valid terms and conditions link while using the feature */
    const val REFERRAL_TANDC_LINK = "https://www.google.co.in/"
}