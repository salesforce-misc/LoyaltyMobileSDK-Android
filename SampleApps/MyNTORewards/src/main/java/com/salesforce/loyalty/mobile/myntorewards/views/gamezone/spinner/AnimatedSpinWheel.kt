package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerBackground
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelLandingPageFooter
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelPointer
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_BORDER_WIDTH
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_FRAME_WIDTH
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_SIZE
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerLandingPageHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Wheel(navController: NavHostController, gameViewModel: GameViewModelInterface, gamesList: MutableList<String>, colourList: MutableList<Color>)
{
    val state = rememberSpinWheelState(gameViewModel, gamesList.size)

    val scope = rememberCoroutineScope()
    Column {
        val rewardList by remember { mutableStateOf(gamesList) }

        val wheelColours=  rememberUpdatedState(colourList)

        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SpinnerBackground)
                .verticalScroll(rememberScrollState()),
        ) {
            SpinnerLandingPageHeader(navController)
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),contentAlignment = Alignment.Center) {

                SpinWheelCircle(WHEEL_SIZE + WHEEL_BORDER_WIDTH)

                SpinWheelFrame(WHEEL_SIZE+WHEEL_BORDER_WIDTH+WHEEL_FRAME_WIDTH, WHEEL_FRAME_WIDTH)

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
                    scope.launch { state.animate(coroutineScope, rewardList) { pieIndex -> } }
                }
            }
            SpinWheelLandingPageFooter()
        }
    }
}

