package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.CheckOutFlowOrderSelectScreen
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderDetails
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderPlacedUI
import com.salesforce.loyalty.mobile.myntorewards.views.home.HomeScreenLandingView
import com.salesforce.loyalty.mobile.myntorewards.views.home.VoucherFullScreen
import com.salesforce.loyalty.mobile.myntorewards.views.myprofile.MyProfileLandingView
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreOptionsScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ProfileViewScreen
import com.salesforce.loyalty.mobile.myntorewards.views.offers.MyPromotionScreen
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.MoreOptions

@Composable
fun HomeScreenAndCheckOutFlowNavigation(
    bottomTabsNavController: NavController,
    showBottomBar: (bottomBarVisible: Boolean) -> Unit
) {
    val navCheckOutFlowController = rememberNavController()
    NavHost(
        navController = navCheckOutFlowController,
        startDestination = CheckOutFlowScreen.StartCheckoutFlowScreen.route
    )
    {

        composable(route = CheckOutFlowScreen.StartCheckoutFlowScreen.route) {
            showBottomBar(true)
            HomeScreenLandingView(bottomTabsNavController, navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.OrderDetailScreen.route) {
            showBottomBar(false)
            CheckOutFlowOrderSelectScreen(navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.OrderAddressAndPaymentScreen.route) {
            showBottomBar(false)
            OrderDetails(navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.OrderConfirmationScreen.route) {
            showBottomBar(false)
            OrderPlacedUI(navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.VoucherFullScreen.route) {
            showBottomBar(true)
            VoucherFullScreen(navCheckOutFlowController)
        }
    }
}


@Composable
fun PromotionScreenAndCheckOutFlowNavigation(showBottomBar: (bottomBarVisible: Boolean) -> Unit) {
    val navCheckOutFlowController = rememberNavController()
    NavHost(
        navController = navCheckOutFlowController,
        startDestination = CheckOutFlowScreen.StartCheckoutFlowScreen.route
    )
    {

        composable(route = CheckOutFlowScreen.StartCheckoutFlowScreen.route) {


            showBottomBar(true)
            MyPromotionScreen(navCheckOutFlowController)


        }
        composable(route = CheckOutFlowScreen.OrderDetailScreen.route) {
            showBottomBar(false)
            CheckOutFlowOrderSelectScreen(navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.OrderAddressAndPaymentScreen.route) {
            showBottomBar(false)
            OrderDetails(navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.OrderConfirmationScreen.route) {
            showBottomBar(false)
            OrderPlacedUI(navCheckOutFlowController)
        }

    }
}

@Composable
fun MyProfileScreen() {
    val navProfileViewController = rememberNavController()
    NavHost(
        navController = navProfileViewController,
        startDestination = ProfileViewScreen.LandingProfileViewScreen.route
    )
    {

        composable(route = ProfileViewScreen.LandingProfileViewScreen.route) {
            MyProfileLandingView(navProfileViewController)
        }
        composable(route = ProfileViewScreen.BenefitFullScreen.route) {
            MyProfileBenefitFullScreenView(navProfileViewController)
        }
        composable(route = ProfileViewScreen.TransactionFullScreen.route) {
            MyProfileTransactionFullScreenView(navProfileViewController)
        }
        composable(route = CheckOutFlowScreen.VoucherFullScreen.route) {
            VoucherFullScreen(navProfileViewController)
        }
    }


}


@Composable
fun RedeemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TextPurpleLightBG)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(id = R.string.screen_title_redeem),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}


@Composable
fun MoreScreen(showBottomBar: (bottomBarVisible: Boolean) -> Unit) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MoreOptionsScreen.MoreOptions.route
    )
    {

        composable(route = MoreOptionsScreen.MoreOptions.route) {
            MoreOptions(navController)
        }
        composable(route = MoreOptionsScreen.PostLogout.route) {
            showBottomBar(false)
            MainScreenStart()
        }
    }
}