package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen

@Composable
fun MainScreenStart() {
    val navController = rememberNavController()
    Navigation(navController)
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.OnboardingScreen.route)
    {
        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreenBox(navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeTabScreen()
        }
    }
}