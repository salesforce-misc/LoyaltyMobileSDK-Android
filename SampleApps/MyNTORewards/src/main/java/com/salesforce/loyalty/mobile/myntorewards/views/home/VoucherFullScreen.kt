package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.VOUCHER_EXPIRED
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.VOUCHER_ISSUED
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.VOUCHER_REDEEMED
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.voucherEmptyViewMsg
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.VoucherViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.VoucherTabs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VoucherFullScreen(
    navCheckOutFlowController: NavController,
    voucherModel: VoucherViewModelInterface
) {

    var refreshing by remember { mutableStateOf(false) }
    var blurBG by remember { mutableStateOf(0.dp) }
    val refreshScope = rememberCoroutineScope()

    val context: Context = LocalContext.current

    fun refresh() = refreshScope.launch {

        voucherModel.loadVoucher(context, true)

    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(contentAlignment = Alignment.TopCenter) {

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(Color.White)
                .pullRefresh(state)
                .blur(blurBG)
        )
        {
            var isInProgress by remember { mutableStateOf(true) }

            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
                    .clickable {
                        navCheckOutFlowController.popBackStack()
                    }
            )
            Text(
                text = stringResource(R.string.vouchers),
                fontFamily = font_archivo_bold,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.padding(
                    top = 11.5.dp,
                    bottom = 11.5.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            )
            //default tab selected as 0 which is VoucherTabs.TabAvailable
            var selectedTab by remember { mutableStateOf(0) }
            var filterType by remember { mutableStateOf("") }


            Row(modifier = Modifier.background(Color.White)) {

                val tabItems =
                    listOf(
                        VoucherTabs.TabAvailable,
                        VoucherTabs.TabRedeemed,
                        VoucherTabs.TabExpired
                    )

                TabRow(selectedTabIndex = selectedTab,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    containerColor = Color.White,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab])
                                .background(Color.White),
                            height = 2.dp,
                            color = VibrantPurple40
                        )
                    })
                {
                    tabItems.forEachIndexed { index, it ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { androidx.compose.material3.Text(text = stringResource(it.tabName)) },
                            selectedContentColor = VibrantPurple40,
                            unselectedContentColor = TextGray,
                        )
                    }
                }
            }

            val vouchers by voucherModel.voucherLiveData.observeAsState() // collecting livedata as state
            val vouchersFetchStatus by voucherModel.voucherViewState.observeAsState() // collecting livedata as state
            val context: Context = LocalContext.current
            LaunchedEffect(true) {
                voucherModel.loadVoucher(context)
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


            filterType = when (selectedTab) {
                0 -> VOUCHER_ISSUED
                1 -> VOUCHER_REDEEMED
                2 -> VOUCHER_EXPIRED
                else -> VOUCHER_ISSUED
            }
            var filteredVouchers = vouchers?.filter {
                it.status == filterType
            }

            // Redeemed tab
            if (selectedTab == 1) {
                filteredVouchers = filteredVouchers?.filter {
                    it.useDate?.let { date ->
                        Common.isWithinMentionedDay(
                            date,
                            AppConstants.REDEEMED_VOUCHERS_FILTER_DAYS.toLong()
                        )
                    } ?: false
                }
            }

            // Expired tab
            if (selectedTab == 2) {
                filteredVouchers = filteredVouchers?.filter {
                    it.expirationDate?.let { date ->
                        Common.isWithinMentionedDay(
                            date,
                            AppConstants.EXPIRED_VOUCHERS_FILTER_DAYS.toLong()
                        )
                    } ?: false
                }
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

            } else {
                if (filteredVouchers?.isEmpty() == true) {
                    VoucherEmptyView(voucherEmptyViewMsg(selectedTab))
                } else {
                    Column(
                        modifier = Modifier
                            .background(VeryLightPurple)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        filteredVouchers?.let {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .background(VeryLightPurple)
                                    .padding(16.dp)
                                    .width(346.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(filteredVouchers.size) { filteredVoucherItem ->
                                    VoucherView(filteredVouchers[filteredVoucherItem]) {
                                        blurBG = it
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


        }
        PullRefreshIndicator(refreshing, state)
    }
}

@Composable
fun VoucherEmptyView(emptyMsg:Int) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = stringResource(id = emptyMsg)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        androidx.compose.material3.Text(
            text = stringResource(id = emptyMsg),
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
