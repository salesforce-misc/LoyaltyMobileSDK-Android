package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.views.TitleView
import com.salesforce.loyalty.mobile.myntorewards.views.components.CircularProgress
import com.salesforce.loyalty.mobile.myntorewards.views.components.CustomScrollableTab
import com.salesforce.loyalty.mobile.myntorewards.views.components.BottomSheetCustomState
import com.salesforce.loyalty.mobile.myntorewards.views.components.bottomSheetShape
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun MyReferralsScreen(viewModel: MyReferralsViewModel, showBottomBar: (Boolean) -> Unit, showBottomSheet: (Boolean) -> Unit) {
    val viewState by viewModel.uiState.observeAsState(null)
    val programState by viewModel.programState.observeAsState()
    val context = LocalContext.current
    val referralProgramJoined = PrefHelper.customPrefs(context)[AppConstants.REFERRAL_PROGRAM_JOINED, false]
    LaunchedEffect(key1 = true) {
        if (referralProgramJoined == true) {
            viewModel.fetchReferralsInfo(context)
        } else {
            viewModel.fetchReferralProgramStatus(context)
        }
    }


    programState?.let {
        LaunchedEffect(key1 = true) {
            if (it == ReferralProgramType.JOIN_PROGRAM) {
                showBottomSheet(true)
                showBottomBar(false)
            }
        }
    }

    viewState?.let {
        when(it) {
            is MyReferralsViewState.MyReferralsFetchSuccess -> {
                MyReferralsScreenView(it.uiState) {
                    showBottomSheet(true)
                    showBottomBar(false)
                    viewModel.startReferring()
                    viewModel.showDefaultPopup = true
                }
            }

            is MyReferralsViewState.MyReferralsFetchFailure -> {
                ErrorPopup(
                    it.errorMessage ?: stringResource(id = R.string.receipt_scanning_error_desc),
                    tryAgainClicked = { viewModel.fetchReferralsInfo(context) },
                    textButtonClicked = {  }
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
        ReferralTabs.Success.tabIndex -> {
            ReferralList(itemStates = uiState.completedStates)
        }

        ReferralTabs.InProgress.tabIndex -> {
            ReferralList(itemStates = uiState.inProgressStates)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyReferralsListScreen(viewModel: MyReferralsViewModel = hiltViewModel(), backAction: () -> Boolean, showBottomBar: (Boolean) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var blurBG by remember { mutableStateOf(0.dp) }
    var openBottomsheet by remember { mutableStateOf(false) }

    val bottomSheetScaffoldState = androidx.compose.material.rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )
    val openBottomSheet = {
        coroutineScope.launch {
            blurBG = AppConstants.BLUR_BG
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    if (openBottomsheet) {
        openBottomSheet()
    }

    val closeBottomSheet = {
        blurBG = AppConstants.NO_BLUR_BG
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    val context = LocalContext.current
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            ReferFriendScreen(viewModel, backAction = backAction) {
                if (viewModel.programState.value!! == ReferralProgramType.START_REFERRING) {
                    viewModel.fetchReferralsInfo(context)
                }
                viewModel.showDefaultPopup = false
                openBottomsheet = false
                closeBottomSheet()
                showBottomBar(true)
            }
        },
        sheetShape = bottomSheetShape,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .blur(blurBG)
        ) {
            TitleView(stringResource(id = R.string.header_label_my_referrals))
            MyReferralsScreen(viewModel, {
                showBottomBar(it)
            }, {
                openBottomsheet = it
            })
        }
    }
}