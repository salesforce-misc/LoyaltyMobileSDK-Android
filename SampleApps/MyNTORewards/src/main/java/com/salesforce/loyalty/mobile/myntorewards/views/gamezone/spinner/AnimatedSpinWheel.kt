package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerBackground
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.components.ShowErrorDialog
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.GameNameIDDataModel
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelLandingPageFooter
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelPointer
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_BORDER_WIDTH
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_FRAME_WIDTH
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_SIZE
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerLandingPageHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun Wheel(
    navController: NavHostController,
    gameViewModel: GameViewModelInterface,
    gamesList: MutableList<GameNameIDDataModel>,
    colourList: MutableList<Color>,
    gameParticipantRewardId: String
) {
    val state = rememberSpinWheelState(gameViewModel, gamesList.size, gameParticipantRewardId)
    var openAlertDialog by remember { mutableStateOf(false) }

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
                .verticalScroll(rememberScrollState()),
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
                        openAlertDialog = true
                    } }
                }
            }
            /*if(!isWheelTapClicked)
            {*/
                //SpinWheelLandingPageFooter()  commenting as
//            }

        }
    }

    if (openAlertDialog) {
        ShowErrorDialog(
            onDismiss = {
                openAlertDialog = false
                navController.popBackStack()
            },
            title = stringResource(id = R.string.game_error),
            description = stringResource(
                id = R.string.game_error_desc
            ),
            confirmButtonText = stringResource(id = R.string.game_error_dialog_ok),
            confirmButtonClick = {
                openAlertDialog = false
                navController.popBackStack()
            })
    }

}

