package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MY_REFERRALS_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_BENEFIT_FULL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_CAPTURE_IMAGE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_GAME_BETTER_LUCK_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_GAME_CONGRATS_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_GAME_SCRATCH_CARD
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_GAME_SPIN_WHEEL
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_GAME_ZONE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_LANDING_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_MORE_LIST_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_MORE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ONBOARDING_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ORDER_ADDRESS_PAYMENT_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ORDER_CONFIRMATION_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ORDER_DETAIL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_PROFILE_LANDING_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_RECEIPT_DETAIL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_RECEIPT_LIST_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_SCANNED_CONG_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_SCANNED_RECEIPT_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_SCAN_PROGRESS_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_START_CHECKOUT_FLOW_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_TRANSACTION_FULL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_VOUCHER_FULL_SCREEN

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen(ROUTE_ONBOARDING_SCREEN)
    object HomeScreen : Screen(ROUTE_LANDING_SCREEN)
}

sealed class CheckOutFlowScreen(val route: String) {
    object StartCheckoutFlowScreen : CheckOutFlowScreen(ROUTE_START_CHECKOUT_FLOW_SCREEN)
    object OrderDetailScreen : CheckOutFlowScreen(ROUTE_ORDER_DETAIL_SCREEN)
    object OrderAddressAndPaymentScreen : CheckOutFlowScreen(ROUTE_ORDER_ADDRESS_PAYMENT_SCREEN)
    object OrderConfirmationScreen : CheckOutFlowScreen(ROUTE_ORDER_CONFIRMATION_SCREEN)
    object VoucherFullScreen : CheckOutFlowScreen(ROUTE_VOUCHER_FULL_SCREEN)
}

sealed class ProfileViewScreen(val route: String) {
    object LandingProfileViewScreen : ProfileViewScreen(ROUTE_PROFILE_LANDING_SCREEN)
    object BenefitFullScreen : ProfileViewScreen(ROUTE_BENEFIT_FULL_SCREEN)
    object TransactionFullScreen : ProfileViewScreen(ROUTE_TRANSACTION_FULL_SCREEN)
}

sealed class MoreScreens(val route: String) {
    object MoreScreenOptions : MoreScreens(ROUTE_MORE_LIST_SCREEN)
    object ReceiptListScreen : MoreScreens(ROUTE_RECEIPT_LIST_SCREEN)

    object CaptureImageScreen : MoreScreens(ROUTE_CAPTURE_IMAGE_SCREEN)

    object ScannedReceiptScreen : MoreScreens(ROUTE_SCANNED_RECEIPT_SCREEN)

    object ScannedCongratsScreen : MoreScreens(ROUTE_SCANNED_CONG_SCREEN)

    object ScanningProgressScreen : MoreScreens(ROUTE_SCAN_PROGRESS_SCREEN)

    object ReceiptDetailScreen : MoreScreens(ROUTE_RECEIPT_DETAIL_SCREEN)

    object GameZoneScreen : MoreScreens(ROUTE_GAME_ZONE_SCREEN)

    object ScratchCardScreen : MoreScreens(ROUTE_GAME_SCRATCH_CARD)

    object SpinWheelScreen : MoreScreens(ROUTE_GAME_SPIN_WHEEL)
    object GameCongratsScreen : MoreScreens(ROUTE_GAME_CONGRATS_SCREEN)
    object GameBetterLuckScreen : MoreScreens(ROUTE_GAME_BETTER_LUCK_SCREEN)
    object MyReferralsScreen : MoreScreens(MY_REFERRALS_SCREEN)
}
