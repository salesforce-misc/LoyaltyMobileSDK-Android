package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG

@Composable
fun HomeTabScreen() {
    val navController = rememberNavController()
    Scaffold(
        Modifier.background(TextPurpoleLightBG),
        bottomBar = { BottomNavigationUI(navController) }
    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(TextPurpoleLightBG)
        ) {
            TabNavigation(navController)
        }
    }
}

@Composable
fun TabNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavTabs.Home.route)
    {
        composable(route = BottomNavTabs.Home.route) {

            HomeScreen()
        }
        composable(route = BottomNavTabs.MyOffers.route) {
            MyOfferScreen()
        }
        composable(route = BottomNavTabs.MyProfile.route) {
            MyProfileScreen()
        }
        //part of UX but not part of MVP
      /*  composable(route = BottomNavTabs.Redeem.route) {
            RedeemScreen()
        }*/
        composable(route = BottomNavTabs.More.route) {
            MoreScreen()
        }
    }
}