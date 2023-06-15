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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROFILE_ELEMENT_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BenefitViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MembershipProfileViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.TransactionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.VoucherViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.MyBenefitMiniScreenView
import com.salesforce.loyalty.mobile.myntorewards.views.ScreenTabHeader
import com.salesforce.loyalty.mobile.myntorewards.views.TransactionCard
import com.salesforce.loyalty.mobile.myntorewards.views.UserInfoRow
import com.salesforce.loyalty.mobile.myntorewards.views.home.VoucherRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProfileLandingView(navProfileViewController: NavHostController,
                         profileModel: MembershipProfileViewModelInterface,
                         voucherModel: VoucherViewModelInterface,
                         benefitViewModel: BenefitViewModelInterface,
                         transactionViewModel: TransactionViewModelInterface
) {
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    val context: Context = LocalContext.current //fetching reference of viewmodel

    fun refresh() = refreshScope.launch {
        profileModel.loadProfile(context, true)
        transactionViewModel.loadTransactions(context, true)
        voucherModel.loadVoucher(context,true )
        benefitViewModel.loadBenefits(context, true)
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
    Box(contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(Color.White)
                .pullRefresh(state)
                .verticalScroll(rememberScrollState()).testTag(TEST_TAG_PROFILE_ELEMENT_CONTAINER),
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
                    UserInfoRow(profileModel)
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    ProfileCard(profileModel)
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(MyProfileScreenBG)
                    )
                    TransactionCard(navProfileViewController, transactionViewModel)
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(MyProfileScreenBG)
                    )
                    VoucherRow(navProfileViewController, voucherModel)
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(MyProfileScreenBG)
                    )

                    MyBenefitMiniScreenView(navProfileViewController, benefitViewModel)
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
