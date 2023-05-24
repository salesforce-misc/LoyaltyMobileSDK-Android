package com.salesforce.loyalty.mobile.myntorewards.views.myprofile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.views.MyBenefitMiniScreenView
import com.salesforce.loyalty.mobile.myntorewards.views.ScreenTabHeader
import com.salesforce.loyalty.mobile.myntorewards.views.TransactionCard
import com.salesforce.loyalty.mobile.myntorewards.views.UserInfoRow
import com.salesforce.loyalty.mobile.myntorewards.views.home.VoucherRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProfileLandingView(navProfileViewController: NavHostController) {
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    val profileModel: MembershipProfileViewModel = viewModel()
    val context: Context = LocalContext.current
    val voucherModel: VoucherViewModel = viewModel()
    val tranModel: TransactionsViewModel = viewModel()  //fetching reference of viewmodel
    val benModel: MembershipBenefitViewModel = viewModel()

    fun refresh() = refreshScope.launch {
        profileModel.loadProfile(context, true)
        tranModel.loadTransactions(context, true)
        voucherModel.loadVoucher(context,true )
        benModel.loadBenefits(context, true)
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
    Box(contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(Color.White)
                .pullRefresh(state)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )

        {

            Spacer(modifier = Modifier.height(50.dp))
            ScreenTabHeader()

            Box(contentAlignment = Alignment.TopCenter) {

                Column(
                ) {

                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    UserInfoRow()
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    ProfileCard()
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(MyProfileScreenBG)
                    )
                    TransactionCard(navProfileViewController)
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(MyProfileScreenBG)
                    )
                    VoucherRow(navProfileViewController)
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(MyProfileScreenBG)
                    )

                    MyBenefitMiniScreenView(navProfileViewController)
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(MyProfileScreenBG)
                    )


                }

            }


        }
        PullRefreshIndicator(refreshing, state)
    }


}
