package com.salesforce.loyalty.mobile.myntorewards.utilities

class AppConstants {

    companion object {

        //preferences
        const val PREF_NAME = "com.salesforce.loyalty.mobile.sources.SHARED_PREF"
        const val KEY_PROGRAM_MEMBER_ID = "loyaltyProgramMemberId_key"
        const val KEY_MEMBERSHIP_NUMBER = "membershipNumber_key"
        const val KEY_PROGRAM_NAME = "programName_key"
        const val KEY_EMAIL_ID = "emailID_key"
        const val KEY_FIRSTNAME = "firstName_key"
        const val KEY_LASTNAME = "lastName_key"


        //Bottom Nav Tabs Route
        const val ROUTE_HOME_SCREEN = "home_screen"
        const val ROUTE_OFFER_SCREEN = "my_offer_screen"
        const val ROUTE_PROFILE_SCREEN = "my_profile_screen"
        const val ROUTE_REDEEM_SCREEN = "redeem_screen"
        const val ROUTE_MORE_SCREEN = "more_screen"

        //Onboarding screen tab route
        const val ROUTE_ONBOARDING_SCREEN = "onboarding_screen"
        const val ROUTE_LANDING_SCREEN = "landing_screen"

        //benefit icon based on strings
        const val BENEFIT_TYPE_FREE_SHIPPING = "Free Shipping"
        const val BENEFIT_TYPE_EXTENDED_RETURN = "Extended Return"
        const val BENEFIT_TYPE_FREE_SAMPLE = "Free Sample"
        const val BENEFIT_TYPE_FREE_SUBSCRIPTION = "Free Subscription"
        const val BENEFIT_TYPE_OFFER = "Tier Exclusive Offer"
        const val BENEFIT_TYPE_SUPPORT = "Support"
        const val BENEFIT_TYPE_COMPLIMENT_VOUCHER = "Complimentary Vouchers"

        //benefit tiers

        const val TIER_SILVER = "silver"
        const val TIER_GOLD = "gold"
        const val TIER_BRONZE = "bronze"
        const val TIER_PLATINUM = "platinum"
        const val TIER_RUBY = "ruby"
        const val MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED = "EligibleButNotEnrolled"
        const val MEMBER_ELIGIBILITY_CATEGORY_ELIGIBLE = "Eligible"
        const val MAX_PAGE_COUNT_PROMOTION = 4
        const val PROMOTION_DATE_API_FORMAT = "yyyy-MM-dd"
        const val PROMOTION_DATE_SAMPLE_APP_FORMAT = "dd/MM/yyyy"




    }
}