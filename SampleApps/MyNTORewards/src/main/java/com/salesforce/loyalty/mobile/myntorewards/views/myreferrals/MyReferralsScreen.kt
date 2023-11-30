package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.views.TitleView
import com.salesforce.loyalty.mobile.myntorewards.views.components.CircularProgress
import com.salesforce.loyalty.mobile.myntorewards.views.components.CustomScrollableTab
import com.salesforce.loyalty.mobile.myntorewards.views.components.BottomSheetCustomState
import com.salesforce.loyalty.mobile.myntorewards.views.components.bottomSheetShape
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ScanningErrorPopup
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun MyReferralsScreen(viewModel: MyReferralsViewModel = viewModel(), showBottomSheet: (Boolean) -> Unit) {
    val viewState by viewModel.uiState.observeAsState(null)

    viewState?.let {
        when(it) {
            is MyReferralsViewState.MyReferralsFetchSuccess -> {
                MyReferralsScreenView(it.uiState) {
                    showBottomSheet(true)
                }
            }

            is MyReferralsViewState.MyReferralsFetchFailure -> {
                ScanningErrorPopup(
                    it.errorMessage,
                    closePopup = { },
                    scanAnotherReceipt = {  }
                )
            }

            is MyReferralsViewState.MyReferralsFetchInProgress -> {
                CircularProgress()
            }
        }
    }
}

@Composable
fun MyReferralsScreenView(uiState: MyReferralScreenState, openReferFriendSheet: () -> Unit) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyReferralsListScreen(viewModel: MyReferralsViewModel = viewModel(), showBottomBar: (Boolean) -> Unit) {
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

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            ReferFriendScreen { showBottomSheet(false) }
        },
        sheetShape = bottomSheetShape,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TitleView(stringResource(id = R.string.header_label_my_referrals))
            MyReferralsScreen(viewModel) {
                showBottomSheet(true)
            }
        }
    }
}