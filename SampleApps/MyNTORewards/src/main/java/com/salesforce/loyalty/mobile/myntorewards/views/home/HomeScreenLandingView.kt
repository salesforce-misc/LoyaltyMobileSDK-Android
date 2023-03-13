package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.HomeScreenState
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MAX_PAGE_COUNT_PROMOTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatPromotionDate
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel

@Composable
fun HomeScreenLandingView(
    navController: NavController,
    openHomeScreen: (homeScreenState: HomeScreenState) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(MyProfileScreenBG)
            .verticalScroll(rememberScrollState())
    )
    {
        Spacer(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(VibrantPurple40)
        )

        AppLogoAndSearchRow()

        UserNameAndRewardRow()

        Spacer(modifier = Modifier.height(16.dp))

        PromotionCardRow(navController)
        VoucherRow(navController) {
            openHomeScreen(it)
        }


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


        val promListListSize = membershipPromo?.size ?: 0
        val pagerState = rememberPagerState()

        val pageCount = if(promListListSize>MAX_PAGE_COUNT_PROMOTION) { MAX_PAGE_COUNT_PROMOTION } else{ promListListSize }

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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun VoucherRow(
    navController: NavController,
    openHomeScreen: (homeScreenState: HomeScreenState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .background(PromotionCardBG)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {

        HomeSubViewHeaderVoucher("Voucher", navController)
        {
            openHomeScreen(it)
        }
        val model: MyPromotionViewModel = viewModel()
        // val membershipPromo by model.membershipPromotionLiveData.observeAsState() // collecting livedata as state
        //val context: Context = LocalContext.current

        //model.promotionAPI(context)

        //val pageCount = membershipPromo?.size ?: 0
        val pagerState = rememberPagerState()

        /* membershipPromo?.let {
             HorizontalPager(count = pageCount, state = pagerState) { page ->
                 PromotionCard(page, membershipPromo)
             }
         }*/


        //dummy voucher number for testing.
        listOf(1, 2, 3).let {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(it) {
                    Spacer(modifier = Modifier.width(12.dp))
                    VoucherView(it, "")
                }
            }
        }

        /*    HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = VibrantPurple40,
                inactiveColor = VibrantPurple90,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )*/

        Spacer(modifier = Modifier.height(16.dp))
    }

}