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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.PromotionScreenState
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.CheckOutFlowOrderSelectScreen
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderDetails
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderPlacedUI
import com.salesforce.loyalty.mobile.myntorewards.views.home.HomeScreenLandingView
import com.salesforce.loyalty.mobile.myntorewards.views.offers.MyPromotionScreen
import com.salesforce.loyalty.mobile.myntorewards.views.home.VoucherFullScreen

@Composable
fun HomeScreen(navController: NavController) {

    var currentHomeState by remember { mutableStateOf(PromotionScreenState.MAIN_VIEW) }

    when (currentHomeState) {
        PromotionScreenState.MAIN_VIEW -> HomeScreenLandingView(navController) {
            currentHomeState = it
        }
        PromotionScreenState.VOUCHER_VIEW -> VoucherFullScreen {
            currentHomeState = it
        }
        PromotionScreenState.CHECKOUT_VIEW -> CheckOutFlowOrderSelectScreen {
            currentHomeState = it
        }
        PromotionScreenState.ADDRESS_PAYMENT_VIEW -> OrderDetails {
            currentHomeState = it
        }
        PromotionScreenState.ORDER_CONFIRMATION_VIEW -> OrderPlacedUI {
            currentHomeState = it
        }
    }
}

@Composable
fun MyOfferScreen(openHomeScreen: (promotionScreenState: PromotionScreenState) -> Unit) {
    val model: MyPromotionViewModel = viewModel()
    val membershipPromo by model.membershipPromotionLiveData.observeAsState() // collecting livedata as state
    val context: Context = LocalContext.current
    model.loadPromotions(context)

    /*MyPromotionScreen(membershipPromo)
    {
        openHomeScreen(it)
    }*/

    var currentHomeState by remember { mutableStateOf(PromotionScreenState.MAIN_VIEW) }

    when (currentHomeState) {
        PromotionScreenState.MAIN_VIEW -> MyPromotionScreen(membershipPromo) {
            openHomeScreen(it)
            currentHomeState = it
        }
        PromotionScreenState.CHECKOUT_VIEW -> CheckOutFlowOrderSelectScreen {
            currentHomeState = it
        }
        PromotionScreenState.ADDRESS_PAYMENT_VIEW -> OrderDetails {
            currentHomeState = it
        }
        PromotionScreenState.ORDER_CONFIRMATION_VIEW -> OrderPlacedUI {
            currentHomeState = it
        }
        else -> {}
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
            .background(TextPurpoleLightBG)
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
            .background(TextPurpoleLightBG)
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