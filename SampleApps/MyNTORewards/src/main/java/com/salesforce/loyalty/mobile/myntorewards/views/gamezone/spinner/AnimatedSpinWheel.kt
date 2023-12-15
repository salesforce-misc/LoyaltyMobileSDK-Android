package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerBackground
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_BG
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.GameNameIDDataModel
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelLandingPageFooter
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelPointer
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_BORDER_WIDTH
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_FRAME_WIDTH
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_SIZE
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerLandingPageHeader
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Wheel(
    navController: NavHostController,
    gameViewModel: GameViewModelInterface,
    gamesList: MutableList<GameNameIDDataModel>,
    colourList: MutableList<Color>,
    gameParticipantRewardId: String
) {
    val state = rememberSpinWheelState(gameViewModel, gamesList.size, gameParticipantRewardId)
    var openBottomsheet by remember { mutableStateOf(false) }

    val bottomSheetScaffoldState = androidx.compose.material.rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )

    val coroutineScope = rememberCoroutineScope()
    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    if (openBottomsheet) {
        openBottomSheet()
    }

    val closeBottomSheet = {
        //showBottomBar(true)
        //blurBG = AppConstants.NO_BLUR_BG // need confirmation if background blur is needed
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    androidx.compose.material.BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            ErrorPopup(stringResource(id = R.string.game_error_msg), textButtonClicked = {},
                tryAgainClicked = { openBottomsheet = false
                    closeBottomSheet()
                    navController.popBackStack() })
        },
        sheetShape = RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE, AppConstants.POPUP_ROUNDED_CORNER_SIZE, 0.dp, 0.dp),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ){

        val scope = rememberCoroutineScope()
        Column {
            val rewardList by remember { mutableStateOf(gamesList) }

            val wheelColours = rememberUpdatedState(colourList)

            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            var isWheelTapClicked by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpinnerBackground)
                    .verticalScroll(rememberScrollState()).testTag(TEST_TAG_SPIN_WHEEL_BG),
            ) {
                SpinnerLandingPageHeader(navController)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp), contentAlignment = Alignment.Center
                ) {

                    SpinWheelCircle(WHEEL_SIZE + WHEEL_BORDER_WIDTH)

                    SpinWheelFrame(
                        WHEEL_SIZE + WHEEL_BORDER_WIDTH + WHEEL_FRAME_WIDTH,
                        WHEEL_FRAME_WIDTH
                    )

                    SpinWheelColourSegment(
                        WHEEL_SIZE,
                        state.pieCount,
                        state.rotation.value,
                        wheelColours.value
                    )
                    SpinWheelContent(
                        spinSize = WHEEL_SIZE,
                        pieCount = state.pieCount,
                        rotationDegree = state.rotation.value,
                        rewardList
                    )
                    SpinWheelPointer() {
                        isWheelTapClicked= true
                        scope.launch { state.animate(coroutineScope, rewardList, navController){
                            //blurBG = AppConstants.BLUR_BG // need confirmation
                            openBottomsheet = true
                        } }
                    }
                }
                SpinWheelLandingPageFooter()

            }

            }
        }
    }

