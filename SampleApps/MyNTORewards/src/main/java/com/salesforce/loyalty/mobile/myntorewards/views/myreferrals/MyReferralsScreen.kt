package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.salesforce.loyalty.mobile.myntorewards.views.components.BottomSheetCustomState
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ScanningErrorPopup
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyReferralsScreen(navController: NavHostController, showBottomBar: (Boolean) -> Unit) {
    val viewModel: MyReferralsViewModel = viewModel()
    val viewState by viewModel.uiState.observeAsState(null)

    val bottomSheetScaffoldState = BottomSheetCustomState()
    val coroutineScope = rememberCoroutineScope()

    // Show Bottom Bar on screen load and hide it when bottom sheet is opened
    showBottomBar(true)

    val showBottomSheet: (Boolean) -> Job = {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.run {
                showBottomBar(!it)
                if (it && isCollapsed) {
                    expand()
                } else {
                    collapse()
                }
            }
        }
    }

    viewState?.let { viewState ->
        when (viewState) {
            is MyReferralsViewState.MyReferralsFetchSuccess -> {
                BottomSheetScaffold(
                    scaffoldState = bottomSheetScaffoldState,
                    sheetContent = {
                        ReferFriendScreen { showBottomSheet(false) }
                    },
                    sheetShape = RoundedCornerShape(22.dp, 22.dp, 0.dp, 0.dp),
                    sheetPeekHeight = 0.dp,
                    sheetGesturesEnabled = false
                ) {
                    MyReferralsScreenView(viewState.uiState) {
                        showBottomSheet(true)
                    }
                }
            }

            is MyReferralsViewState.MyReferralsFetchFailure -> {
                ScanningErrorPopup(
                    viewState.errorMessage,
                    closePopup = { },
                    scanAnotherReceipt = {  }
                )
            }

            is MyReferralsViewState.MyReferralsFetchInProgress -> {
                MyReferralsProgressView()
            }
        }
    }
}

@Composable
fun MyReferralsScreenView(uiState: MyReferralScreenState, openReferFriendSheet: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TitleView(stringResource(id = R.string.header_label_my_referrals))
        Box(
            modifier = Modifier
                .background(VeryLightPurple)
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            ReferralCard(uiState.referralsCountList, uiState.referralsRecentDuration) {
                openReferFriendSheet()
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TitleView(stringResource(id = R.string.header_label_my_referrals))
        CircularProgress()
    }
}