package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.BottomNavTabs

@Composable
fun HomeTabScreen() {
    val bottomTabsNavController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    Scaffold(
        Modifier.background(TextPurpleLightBG),
        bottomBar = { BottomNavigationUI(bottomTabsNavController, bottomBarState) }

    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(TextPurpleLightBG)
        ) {
            TabNavigation(bottomTabsNavController){
                bottomBarState.value= it
            }
        }
    }
}

@Composable
fun TabNavigation(bottomTabsNavController: NavHostController, showBottomBar: (bottomBarVisible:Boolean) -> Unit) {

    NavHost(navController = bottomTabsNavController, startDestination = BottomNavTabs.Home.route)
    {
        composable(route = BottomNavTabs.Home.route) {

            HomeScreenAndCheckOutFlowNavigation(bottomTabsNavController){
                showBottomBar(it)
            }
        }
        composable(route = BottomNavTabs.MyOffers.route) {
            PromotionScreenAndCheckOutFlowNavigation{
                showBottomBar(it)
            }
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