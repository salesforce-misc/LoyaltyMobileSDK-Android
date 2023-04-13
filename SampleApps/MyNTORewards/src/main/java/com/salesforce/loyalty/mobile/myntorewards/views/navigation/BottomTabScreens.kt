package com.salesforce.loyalty.mobile.myntorewards.views

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.CheckOutFlowOrderSelectScreen
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderDetails
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderPlacedUI
import com.salesforce.loyalty.mobile.myntorewards.views.home.HomeScreenLandingView
import com.salesforce.loyalty.mobile.myntorewards.views.offers.MyPromotionScreen
import com.salesforce.loyalty.mobile.myntorewards.views.home.VoucherFullScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

@Composable
fun HomeScreenAndCheckOutFlowNavigation(bottomTabsNavController: NavController, showBottomBar: (bottomBarVisible:Boolean) -> Unit)
{
    val navCheckOutFlowController = rememberNavController()
    NavHost(navController = navCheckOutFlowController, startDestination = CheckOutFlowScreen.StartCheckoutFlowScreen.route)
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
fun PromotionScreenAndCheckOutFlowNavigation(showBottomBar: (bottomBarVisible:Boolean) -> Unit)
{
    val navCheckOutFlowController = rememberNavController()
    NavHost(navController = navCheckOutFlowController, startDestination = CheckOutFlowScreen.StartCheckoutFlowScreen.route)
    {

        composable(route = CheckOutFlowScreen.StartCheckoutFlowScreen.route) {
            showBottomBar(true)
            val model: MyPromotionViewModel = viewModel()
            val context: Context = LocalContext.current
            val promoViewState by model.promotionViewState.observeAsState()
            LaunchedEffect(true) {
                model.loadPromotions(context)
            }
            var membershipPromo: List<Results>? = mutableListOf()
            when (promoViewState) {
                is PromotionViewState.PromotionsFetchSuccess -> {
                    membershipPromo = (promoViewState as PromotionViewState.PromotionsFetchSuccess).response?.outputParameters?.outputParameters?.results
                }
                else -> {}
            }
            MyPromotionScreen(membershipPromo, navCheckOutFlowController)
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
    var currentMyProfileState by remember { mutableStateOf(MyProfileScreenState.MAIN_VIEW) }

    when (currentMyProfileState) {
        MyProfileScreenState.MAIN_VIEW -> MyProfileLandingView {
            currentMyProfileState = it
        }
        MyProfileScreenState.BENEFIT_VIEW -> MyProfileBenefitFullScreenView {
            currentMyProfileState = it
        }
        MyProfileScreenState.TRANSACTION_VIEW -> MyProfileTransactionFullScreenView {
            currentMyProfileState = it
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
fun MoreScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TextPurpleLightBG)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(id = R.string.screen_title_more),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}