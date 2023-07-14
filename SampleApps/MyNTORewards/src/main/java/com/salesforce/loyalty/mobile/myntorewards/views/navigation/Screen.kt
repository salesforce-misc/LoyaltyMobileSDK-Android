package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_BENEFIT_FULL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_LANDING_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_MORE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ONBOARDING_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ORDER_ADDRESS_PAYMENT_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ORDER_CONFIRMATION_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ORDER_DETAIL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_PROFILE_LANDING_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_RECEIPT_LIST_SCREEN
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
    object MoreScreenOptions : MoreScreens(ROUTE_MORE_SCREEN)
    object ReceiptListScreen : MoreScreens(ROUTE_RECEIPT_LIST_SCREEN)
}
