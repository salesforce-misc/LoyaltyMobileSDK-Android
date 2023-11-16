package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.CircularProgress
import com.salesforce.loyalty.mobile.myntorewards.views.components.CustomScrollableTab
import com.salesforce.loyalty.mobile.myntorewards.views.components.HeaderText
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs

@Composable
fun MyReferralsScreen() {
    val viewModel: MyReferralsViewModel = viewModel()
    val viewState by viewModel.uiState.observeAsState(null)

    viewState?.let {
        when(it) {
            is MyReferralsViewState.MyReferralsFetchSuccess -> {
                MyReferralsScreenView(it.uiState)
            }
            is MyReferralsViewState.MyReferralsFetchFailure -> {

            }
            is MyReferralsViewState.MyReferralsFetchInProgress -> {
                CircularProgress()
            }
        }
    }
}

@Composable
fun MyReferralsScreenView(uiState: MyReferralScreenState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        HeaderText(
            text = stringResource(id = R.string.header_label_my_referrals),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Box(modifier = Modifier
            .background(VeryLightPurple)
            .padding(vertical = 12.dp, horizontal = 24.dp)) {
            ReferralCard(uiState.referralsCountList)
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MyReferralsScreenPreview() {
//    MyReferralsScreenView(viewModel.uiState())
}