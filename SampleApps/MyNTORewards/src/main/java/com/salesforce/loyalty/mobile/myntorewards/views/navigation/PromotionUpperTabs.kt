package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.salesforce.loyalty.mobile.myntorewards.views.HomeScreen
import com.salesforce.loyalty.mobile.myntorewards.views.MoreScreen
import com.salesforce.loyalty.mobile.myntorewards.views.MyOfferScreen
import com.salesforce.loyalty.mobile.myntorewards.views.MyProfileScreen


sealed class PromotionUpperTabs(var titleID: String, var route: String) {
    object Home : PromotionUpperTabs(
       "All", "all"
    )
    object Active : PromotionUpperTabs(
        "Active", "active"
    )
    object Unenrolled : PromotionUpperTabs(
        "Unenrolled", "unenrolled"
    )
}

@Composable
fun PromotionTabNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = PromotionUpperTabs.Home.route)
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

sealed class Tabs(val tabName:String, val selected:Boolean)
{
    object Tab1 :Tabs("All", false)
    object Tab2 :Tabs("Active", false)
    object Tab3 :Tabs("Unenrolled", false)
}