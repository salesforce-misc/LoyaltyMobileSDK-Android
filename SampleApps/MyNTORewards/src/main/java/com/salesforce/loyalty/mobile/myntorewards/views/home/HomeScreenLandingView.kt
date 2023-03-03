package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel

@Composable
fun HomeScreenLandingView(navController: NavController) {

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(MyProfileScreenBG)
    ) {

        Spacer(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(VibrantPurple40))

        AppLogoAndSearchRow()

        UserNameAndRewardRow()

        Spacer(modifier = Modifier.height(16.dp))

        PromotionCardRow(navController)

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PromotionCardRow(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .background(PromotionCardBG)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {

        HomeSubViewHeader("Promotions", navController)
        val model: MyPromotionViewModel = viewModel()
        val membershipPromo by model.membershipPromotionLiveData.observeAsState() // collecting livedata as state
        val context: Context = LocalContext.current

        model.promotionAPI(context)

        val pageCount = membershipPromo?.size ?: 0
        val pagerState = rememberPagerState()

        membershipPromo?.let {
            HorizontalPager(count = pageCount, state = pagerState) { page ->
                PromotionCard(page, membershipPromo)
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = VibrantPurple40,
            inactiveColor = VibrantPurple90,
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

}