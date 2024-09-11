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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_DEFAULT_PROMOTION_ID
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.views.TitleView
import com.salesforce.loyalty.mobile.myntorewards.views.components.CircularProgress
import com.salesforce.loyalty.mobile.myntorewards.views.components.CustomScrollableTab
import com.salesforce.loyalty.mobile.myntorewards.views.components.bottomSheetShape
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup
import kotlinx.coroutines.launch

const val LOG_TAG = "MyReferralsScreen"

/**
 * Referrals feature main screen, where users can see total no.of referrals sent, friends signed-up and completed details.
 * Users can also enroll to the configured default promotion and refer friends
 */
@Composable
fun MyReferralsScreen(
    viewModel: MyReferralsViewModel,
    showBottomBar: (Boolean) -> Unit,
    showBottomSheet: (Boolean) -> Unit,
    backAction: () -> Boolean
) {
    val viewState by viewModel.uiState.observeAsState(null)
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.checkIfReferralIsEnabled()
    }
    viewState?.let {
        when(it) {
            is MyReferralsViewState.MyReferralsFetchSuccess -> {
                MyReferralsScreenView(it.uiState) {
                    showBottomSheet(true)
                    showBottomBar(false)
                }
            }

            is MyReferralsViewState.MyReferralsFetchFailure -> {
                ShowErrorScreen(stringResource(id = R.string.game_error_msg), backAction)
            }

            is MyReferralsViewState.MyReferralsFetchInProgress -> {
                CircularProgress()
            }

            is MyReferralsViewState.MyReferralsPromotionEnrolled -> {
                viewModel.fetchReferralsInfo(context)
            }

            is MyReferralsViewState.MyReferralsPromotionNotEnrolled -> {
                viewModel.fetchReferralsInfo(context)
            }

            is MyReferralsViewState.MyReferralsPromotionStatusFailure -> {
                ShowErrorScreen(it.errorMessage, backAction)
            }

            is MyReferralsViewState.PromotionReferralApiStatusFailure -> {
                ShowErrorScreen(it.error, backAction)
            }
            MyReferralsViewState.PromotionStateNonReferral, MyReferralsViewState.ReferralFeatureNotEnabled -> {
                ShowErrorScreen(stringResource(id = R.string.referral_feature_not_enabled_error_message), backAction)
            }
            MyReferralsViewState.ReferralFeatureEnabled -> {
                viewModel.checkIfGivenPromotionIsInCache(context, REFERRAL_DEFAULT_PROMOTION_ID)
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
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    fun refresh() = refreshScope.launch {
        viewModel.checkIfReferralIsEnabled(true)
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            val promotionDetails = viewModel.fetchReferralPromotionDetailsFromCache(context, REFERRAL_DEFAULT_PROMOTION_ID)
            ReferFriendScreen(viewModel, promotionDetails = promotionDetails, backAction = backAction) {
                if (viewModel.programState.value!! == ReferralProgramType.START_REFERRING
                    && viewModel.forceRefreshReferralsInfo) {
                    viewModel.fetchReferralsInfo(context)
                }
                openBottomsheet = false
                closeBottomSheet()
                showBottomBar(true)
            }
        },
        sheetShape = bottomSheetShape,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(state)
                    .background(Color.White)
                    .blur(blurBG)
            ) {
                TitleView(stringResource(id = R.string.header_label_my_referrals))
                MyReferralsScreen(viewModel, {
                    showBottomBar(it)
                }, {
                    openBottomsheet = it
                }, backAction)
            }
            PullRefreshIndicator(refreshing, state)
        }
    }
}

@Composable
private fun ShowErrorScreen(errorMessage: String?, closePopup: () -> Boolean) {
    ErrorPopup(
        errorMessage ?: stringResource(id = R.string.game_error_msg),
        tryAgainClicked = { closePopup() },
        tryAgainButtonText = stringResource(id = R.string.referral_back_button_text),
        textButtonClicked = { }
    )
}