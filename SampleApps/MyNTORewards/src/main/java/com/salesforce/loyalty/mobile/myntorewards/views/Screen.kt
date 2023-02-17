package com.salesforce.loyalty.mobile.myntorewards.views

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_LANDING_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_ONBOARDING_SCREEN

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen(ROUTE_ONBOARDING_SCREEN)
    object HomeScreen : Screen(ROUTE_LANDING_SCREEN)
}