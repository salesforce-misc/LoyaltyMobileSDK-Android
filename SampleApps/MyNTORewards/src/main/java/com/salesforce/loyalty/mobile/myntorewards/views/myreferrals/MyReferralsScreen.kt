package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.views.TitleView
import com.salesforce.loyalty.mobile.myntorewards.views.components.CircularProgress
import com.salesforce.loyalty.mobile.myntorewards.views.components.CustomScrollableTab

@Composable
fun MyReferralsScreen(navController: NavHostController) {
    val viewModel: MyReferralsViewModel = viewModel()
    val viewState by viewModel.uiState.observeAsState(null)

    viewState?.let {
        when(it) {
            is MyReferralsViewState.MyReferralsFetchSuccess -> {
                MyReferralsScreenView(it.uiState, navController)
            }
            is MyReferralsViewState.MyReferralsFetchFailure -> {
                // TODO: Handle Error Scenario
            }
            is MyReferralsViewState.MyReferralsFetchInProgress -> {
                MyReferralsProgressView()
            }
        }
    }
}

@Composable
fun MyReferralsScreenView(uiState: MyReferralScreenState, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        TitleView(stringResource(id = R.string.header_label_my_referrals))
        Box(
            modifier = Modifier
                .background(VeryLightPurple)
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            ReferralCard(uiState.referralsCountList, uiState.referralsRecentDuration) {
                // TODO: Navigation to Refer a Friend Screen
            }
        }

        var selectedTab by remember { mutableStateOf(0) }
        CustomScrollableTab(uiState.tabItems, selectedTab) { tab -> selectedTab = tab }
        when (selectedTab) {
            0 -> {
                ReferralList(itemStates = uiState.completedStates)
            }

            1 -> {
                ReferralList(itemStates = uiState.inProgressStates)
            }
        }
    }
}

@Composable
fun MyReferralsProgressView() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        TitleView(stringResource(id = R.string.header_label_my_referrals))
        CircularProgress()
    }
}