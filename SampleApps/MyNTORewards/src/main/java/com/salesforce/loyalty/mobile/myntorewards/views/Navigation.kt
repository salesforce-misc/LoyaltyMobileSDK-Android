package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextDarkGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40


@Composable
fun Navigation(navController: NavHostController)
{
    NavHost(navController=navController, startDestination= Screen.OnboardingScreen.route)
    {
        composable(route = Screen.OnboardingScreen.route){
            MainScreen(navController)
        }
        composable(route = Screen.HomeScreen.route){
            HomeTabScreen()
           // HomeScreen(navController)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(route = BottomNavTabs.Home.route){
            HomeScreen()
        }
        composable(route = BottomNavTabs.MyOffers.route){
            MyOfferScreen()
        }
        composable(route = BottomNavTabs.MyProfile.route){
           MyProfileScreen()
        }
        composable(route = BottomNavTabs.Redeem.route){
            MyOfferScreen()
        }
        composable(route = BottomNavTabs.More.route){
           RedeemScreen()
        }
    }
}

@Composable
fun MainScreen(navController: NavController){
   // OnboardingScreenBox(navController)
  // HomeScreen(navController)
}

@Composable
fun LoginScreen(navController: NavController){
        //LoginUI(navController)
}

@Composable
fun HomeTabScreen(){
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
        ){
            Navigation(navController)
        }

    }
}


@Composable
fun BottomNavigationUI(navController: NavController) {
    val items = listOf(
        BottomNavTabs.Home,
        BottomNavTabs.MyOffers,
        BottomNavTabs.MyProfile,
        BottomNavTabs.Redeem,
        BottomNavTabs.More
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.Black,
        modifier = Modifier.height(83.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title, modifier = Modifier.size(24.dp, 24.dp)) },
                label = { Text(text = item.title,
                    fontSize = 12.sp)},
                selectedContentColor = VibrantPurple40,
                unselectedContentColor =TextDarkGray,
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
