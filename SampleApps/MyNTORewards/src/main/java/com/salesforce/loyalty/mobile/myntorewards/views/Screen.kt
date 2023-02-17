package com.salesforce.loyalty.mobile.myntorewards.views

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen("onboarding_screen")
    object HomeScreen : Screen("home_screen")
}