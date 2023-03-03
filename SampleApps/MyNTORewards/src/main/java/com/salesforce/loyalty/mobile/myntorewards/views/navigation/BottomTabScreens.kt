package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.views.home.HomeScreenLandingView
import com.salesforce.loyalty.mobile.myntorewards.views.home.MyPromotionScreen

@Composable
fun HomeScreen(navController: NavController) {
    HomeScreenLandingView(navController)
}

@Composable
fun MyOfferScreen() {
    MyPromotionScreen()
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