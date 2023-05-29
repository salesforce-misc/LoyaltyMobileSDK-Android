package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MAX_PAGE_COUNT_PROMOTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.VOUCHER_EXPIRED
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipProfileViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.VoucherViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenLandingView(
    bottomTabsNavController: NavController,
    navCheckOutFlowController: NavController,
) {
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    val model: MyPromotionViewModel = viewModel()
    val profileModel: MembershipProfileViewModel = viewModel()
    val context: Context = LocalContext.current
    val voucherModel: VoucherViewModel = viewModel()


    fun refresh() = refreshScope.launch {
        model.loadPromotions(context, true)
        profileModel.loadProfile(context, true)
        voucherModel.loadVoucher(context, true)
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)


    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(MyProfileScreenBG)
            .pullRefresh(state)
            .verticalScroll(rememberScrollState())
    )
    {

        Spacer(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(VibrantPurple40)
        )

        Box(contentAlignment = Alignment.TopCenter) {

            Column {

                AppLogoAndSearchRow()
                UserNameAndRewardRow()

                PromotionCardRow(bottomTabsNavController, navCheckOutFlowController)
                VoucherRow(navCheckOutFlowController)
            }

            PullRefreshIndicator(refreshing, state)

        }


    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun PromotionCardRow(
    bottomTabsNavController: NavController,
    navCheckOutFlowController: NavController
) {

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .height(450.dp)
            .background(PromotionCardBG)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        var isInProgress by remember { mutableStateOf(false) }
        HomeSubViewHeader(R.string.text_promotions, bottomTabsNavController)
        val model: MyPromotionViewModel = viewModel()
        val context: Context = LocalContext.current

        val promoViewState by model.promotionViewState.observeAsState()
        LaunchedEffect(true) {
            model.loadPromotions(context)
        }

        when (promoViewState) {
            is PromotionViewState.PromotionsFetchSuccess -> {
                isInProgress = false
                val membershipPromo =
                    (promoViewState as PromotionViewState.PromotionsFetchSuccess).response?.outputParameters?.outputParameters?.results

                val promListListSize = membershipPromo?.size ?: 0
                val pagerState = rememberPagerState()

                val pageCount = if (promListListSize > MAX_PAGE_COUNT_PROMOTION) {
                    MAX_PAGE_COUNT_PROMOTION
                } else {
                    promListListSize
                }

                if (membershipPromo?.isEmpty() == true) {
                    PromotionEmptyView(R.string.description_empty_promotions)
                }
                membershipPromo?.let {
                    HorizontalPager(count = pageCount, state = pagerState) { page ->
                        PromotionCard(page, membershipPromo, navCheckOutFlowController)
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

            }
            is PromotionViewState.PromotionsFetchFailure -> {
                isInProgress = false
                PromotionEmptyView(R.string.description_empty_promotions)
                // TODO Show Empty Promotions view
            }
            PromotionViewState.PromotionFetchInProgress -> {
                isInProgress = true
            }
            else -> {}
        }
        if (isInProgress) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.1f)
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
fun VoucherRow(
    navCheckOutFlowController: NavController,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .background(PromotionCardBG)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.text_vouchers),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
            Text(
                text = stringResource(id = R.string.view_all),
                fontWeight = FontWeight.Bold,
                color = VibrantPurple40,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                modifier = Modifier.clickable {
                    navCheckOutFlowController.navigate(CheckOutFlowScreen.VoucherFullScreen.route)
                }
            )
        }

        var isInProgress by remember { mutableStateOf(false) }

        val model: VoucherViewModel = viewModel()
        val vouchers by model.voucherLiveData.observeAsState() // collecting livedata as state
        val vouchersFetchStatus by model.voucherViewState.observeAsState() // collecting livedata as state

        val context: Context = LocalContext.current
        LaunchedEffect(key1 = true) {
            model.loadVoucher(context)
            isInProgress = true
        }

        when (vouchersFetchStatus) {
            VoucherViewState.VoucherFetchSuccess -> {
                isInProgress = false

            }
            VoucherViewState.VoucherFetchFailure -> {
                isInProgress = false
            }

            VoucherViewState.VoucherFetchInProgress -> {
                isInProgress = true
            }
            else -> {}
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isInProgress) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.1f)
                )
            } else {
                if (vouchers?.isEmpty() == true) {
                    VoucherEmptyView()
                }

                vouchers?.let {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        var items= it.filter { it.status !=  VOUCHER_EXPIRED}
                        if(items.size> 2)
                        {
                            items= items.subList(0,2)
                        }
                        items(items) {
                                Spacer(modifier = Modifier.width(12.dp))
                                VoucherView(it)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

}