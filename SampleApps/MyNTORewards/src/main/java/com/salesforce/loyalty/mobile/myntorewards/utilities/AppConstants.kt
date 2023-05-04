package com.salesforce.loyalty.mobile.myntorewards.utilities

class AppConstants {

    companion object {

        //preferences
        const val PREF_NAME = "com.salesforce.loyalty.mobile.sources.SHARED_PREF"
        const val CONNECTED_APP_PREF_NAME = "com.salesforce.loyalty.mobile.myntorewards.CONNECTED_APP_PREF"
        const val AUTH_PREF_NAME = "com.salesforce.loyalty.mobile.myntorewards.AUTH_PREF"
        const val KEY_SELECTED_INSTANCE_URL = "selected_instance_url"
        const val KEY_OPEN_CONNECTED_APP_INSTANCE = "open_connected_app_instance"
        const val KEY_USER_IDENTIFIER = "user_identifier"
        const val KEY_LOGIN_SUCCESSFUL = "login_success"
        const val KEY_COMMUNITY_MEMBER = "community_member_details"

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

        //Transaction types
        const val TRANSACTION_PURCHASE = "Purchase"
        const val TRANSACTION_REDEMPTION = "Redemption"
        const val TRANSACTION_REFERRAL = "Member Referral"
        const val TRANSACTION_VOUCHER = "Voucher"
        const val TRANSACTION_SOCIAL_MEDIA = "Social Media Activity"
        const val TRANSACTION_ENROLLMENT = "Enrollment"
        const val MAX_TRANSACTION_COUNT = 3
        const val TRANSACTION_REWARD_POINTS = "Points"
        const val TRANSACTION_REWARD_CURRENCY_NAME = "Reward Points"
        const val TRANSACTION_HISTORY_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val TRANSACTION_HISTORY_APP_DATE_FORMAT = "dd LLLL yyyy"

        //tabs vouchers
        const val VOUCHER_ISSUED = "Issued"
        const val VOUCHER_REDEEMED = "Redeemed"
        const val VOUCHER_EXPIRED = "Expired"

        //CheckoutScreen Navigation Route
        const val ROUTE_START_CHECKOUT_FLOW_SCREEN = "start_checkout_flow_screen"
        const val ROUTE_ORDER_DETAIL_SCREEN = "order_detail_screen"
        const val ROUTE_ORDER_ADDRESS_PAYMENT_SCREEN = "order_address_payment_screen"
        const val ROUTE_ORDER_CONFIRMATION_SCREEN = "order_confirmation_screen"
        const val ROUTE_VOUCHER_FULL_SCREEN = "voucher_full_screen"

        const val ORDER_ID = "orderID"

        const val TAP_COUNT_OPEN_ADMIN_SETTINGS = 6
    }
}