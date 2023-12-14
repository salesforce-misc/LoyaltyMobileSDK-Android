package com.salesforce.loyalty.mobile.myntorewards.utilities

import androidx.compose.ui.unit.dp

class AppConstants {

    companion object {

        //preferences
        const val PREF_NAME = "com.salesforce.loyalty.mobile.sources.SHARED_PREF"
        const val CONNECTED_APP_PREF_NAME =
            "com.salesforce.loyalty.mobile.myntorewards.CONNECTED_APP_PREF"
        const val AUTH_PREF_NAME = "com.salesforce.loyalty.mobile.myntorewards.AUTH_PREF"
        const val KEY_SELECTED_INSTANCE_URL = "selected_instance_url"
        const val KEY_OPEN_CONNECTED_APP_INSTANCE = "open_connected_app_instance"
        const val KEY_USER_IDENTIFIER = "user_identifier"
        const val KEY_LOGIN_SUCCESSFUL = "login_success"
        const val KEY_COMMUNITY_MEMBER = "community_member_details"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "Phone"

        //Bottom Nav Tabs Route
        const val ROUTE_HOME_SCREEN = "home_screen"
        const val ROUTE_OFFER_SCREEN = "my_offer_screen"
        const val ROUTE_PROFILE_SCREEN = "my_profile_screen"
        const val ROUTE_REDEEM_SCREEN = "redeem_screen"
        const val ROUTE_MORE_SCREEN = "more_screen"

        //Onboarding screen tab route
        const val ROUTE_ONBOARDING_SCREEN = "onboarding_screen"
        const val ROUTE_LANDING_SCREEN = "landing_screen"

        //Profile screen tab route
        const val ROUTE_PROFILE_LANDING_SCREEN = "route_profile_screen"
        const val ROUTE_BENEFIT_FULL_SCREEN = "route_benefit_screen"
        const val ROUTE_TRANSACTION_FULL_SCREEN = "route_transaction_screen"

        //Receipt screens
        const val ROUTE_MORE_LIST_SCREEN = "more_list_screen"
        const val ROUTE_RECEIPT_LIST_SCREEN = "receipt_list_screen"
        const val ROUTE_CAPTURE_IMAGE_SCREEN = "capture_image_screen"
        const val ROUTE_SCANNED_RECEIPT_SCREEN = "scanned_receipt_screen"
        const val ROUTE_SCANNED_CONG_SCREEN = "scanned_cong_screen"
        const val ROUTE_SCAN_PROGRESS_SCREEN = "scanned_progress_screen"
        const val ROUTE_RECEIPT_DETAIL_SCREEN = "receipt_detail_screen"

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

        //Transaction types
        const val TRANSACTION_PURCHASE = "Purchase"
        const val TRANSACTION_REDEMPTION = "Redemption"
        const val TRANSACTION_REFERRAL = "Member Referral"
        const val TRANSACTION_VOUCHER = "Voucher"
        const val TRANSACTION_SOCIAL_MEDIA = "Social Media Activity"
        const val TRANSACTION_ENROLLMENT = "Enrollment"
        const val MAX_TRANSACTION_COUNT = 3
        const val TRANSACTION_REWARD_POINTS = "Points"
        const val REWARD_CURRENCY_NAME = "Reward Points"


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
        const val PROMOTION_NAME = "promotionName"

        const val TAP_COUNT_OPEN_ADMIN_SETTINGS = 6

        const val MAX_SIZE_BENEFIT_LIST = 3

        const val SELF_REGISTER_REDIRECT_URL_PATH = "/apex/CommunitiesLanding"

        const val CHECKOUT_PROMOTION_NAME = "Gold Tier Rejuvenation"

        const val EXPIRED_VOUCHERS_FILTER_DAYS = 30

        const val REDEEMED_VOUCHERS_FILTER_DAYS = 90

        val POPUP_ROUNDED_CORNER_SIZE = 22.dp
        val BLUR_BG = 3.dp
        val NO_BLUR_BG = 0.dp

        //Receipt_Point_Status
        val RECEIPT_POINT_STATUS_PENDING = "Pending"
        val RECEIPT_POINT_STATUS_MANUAL_REVIEW = "Manual Review"
        val RECEIPT_POINT_STATUS_PROCESSING = "Processing"
        val RECEIPT_POINT_STATUS_PROCESSED = "Processed"
        val RECEIPT_POINT_STATUS_REJECTED = "Rejected"

        const val KEY_PROCESSED_AWS_RESPONSE = "key_processed_aws_response"
        const val KEY_PURCHASE_DATE= "key_purchase_date"
        const val KEY_RECEIPT_ID = "key_receipt_id"
        const val KEY_RECEIPT_STATUS = "key_receipt_status"
        const val KEY_RECEIPT_TOTAL_POINTS = "key_receipt_total_points"
        const val KEY_RECEIPT_IMAGE_URL = "key_receipt_image_url"
        const val TAB_ELIGIBLE_ITEM = 0
        const val TAB_ORIGINAL_RECEIPT_IMAGE = 1

        //Different Date Formats incoming from server
        const val PROMOTION_DATE_API_FORMAT = "yyyy-MM-dd"

        const val RECEIPT_DATE_API_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sZ"
        const val TRANSACTION_HISTORY_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val RECEIPT_DETAILS_API_DATETIME_FORMAT = "dd/MM/yyyy"
        const val RECEIPT_DETAILS_API_DATETIME_FORMAT2 = "dd/MM/yyyy"
        const val RECEIPT_API_FORMAT = "yyyy-MM-dd"

        //Date Format 02 Nov 2019
        const val DEFAULT_SAMPLE_APP_FORMAT = "dd MMM yyyy"

        //Date Format 24 August 2023
        const val SAMPLE_APP_DATE_FORMAT_DDLLLYYY  = "dd LLLL yyyy"

        //Date Format 30/09/2023
        const val SAMPLE_APP_DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy"
        const val PREF_MY_DATE = "MyDatePrefs"
        const val KEY_APP_DATE = "appDate"

        const val ROUTE_GAME_ZONE_SCREEN = "game_zone_screen"
        const val ROUTE_GAME_SCRATCH_CARD = "game_scratch_card"
        const val ROUTE_GAME_SPIN_WHEEL = "game_spin_wheel"
        const val ROUTE_GAME_CONGRATS_SCREEN = "game_congrats_screen"
        const val ROUTE_GAME_BETTER_LUCK_SCREEN = "game_better_luck_screen"

        // Game types
        const val SPIN_A_WHEEL_GAME = "SpintheWheel"
        const val SCRATCH_CARD_GAME = "ScratchCard"
        const val GAME_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val TAB_ACTIVE_GAMES = 0
        const val TAB_EXPIRED_GAMES = 1


        const val KEY_GAME_REWARD= "game_reward"

        //Reward Types
        const val VOUCHER = "Voucher"
        const val NO_REWARD = "NoReward"

        const val KEY_GAME_PARTICIPANT_REWARD_ID = "key_game_participant_reward_id"

        //Reward status
        const val REWARD_STATUS_YET_TO_REWARD = "YetToReward"
        const val REWARD_STATUS_REWARDED = "Rewarded"
        const val REWARD_STATUS_NO_REWARD = "NoReward"
        const val REWARD_STATUS_EXPIRED = "Expired"

        const val ROUTE_GAME_ZONE = "route_open_game_zone"

    }
}