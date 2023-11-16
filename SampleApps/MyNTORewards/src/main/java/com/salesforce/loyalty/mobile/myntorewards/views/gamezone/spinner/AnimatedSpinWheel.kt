package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerBackground
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelPointer
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Wheel(navController: NavHostController, gameViewModel: GameViewModelInterface, gamesList: MutableList<String>, colourList: MutableList<Color>)
{
    val state = rememberSpinWheelState(gameViewModel, gamesList.size)

    val scope = rememberCoroutineScope()
    Column {
        val textList by remember { mutableStateOf(gamesList) }
        val frameColor = Color(0xFFFFFFFF)
        val dividerColor = Color.White
        val selectorColor = Color(0xFFFF0000)

        val defaultColours=  DefaultSpinWheelColors(
            frameColor = frameColor,
            dividerColor = dividerColor,
            selectorColor = selectorColor,
            pieColors = colourList
        )
        val defaultDimensions=  DefaultSpinWheelDimensions(
            spinWheelSize = 296.dp,
            frameWidth = 4.dp,
            selectorWidth = 12.dp,
        )
        var coroutineScope: CoroutineScope = rememberCoroutineScope()

        val size= defaultDimensions.spinWheelSize().value
        LaunchedEffect(key1 = state.autoSpinDelay) {
            state.autoSpinDelay?.let {
                delay(it)
                state.spin(coroutineScope){
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SpinnerBackground)
                .verticalScroll(rememberScrollState()),
//            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .wrapContentHeight()
                    .background(SpinnerBackground)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "game_back_button",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .clickable {
                                // Add action for back button press.
                                navController.popBackStack()
                            }
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    Text(
                        text = "Spin a wheel!",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = font_sf_pro
                    )
                    Text(
                        text = stringResource(id = R.string.game_scratch_card_sub_title),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = font_sf_pro
                    )

                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),contentAlignment = Alignment.Center) {

                    SpinWheelCircle(size, defaultDimensions.frameWidth().value)
                    SpinWheelFrame(size, defaultDimensions.frameWidth().value)

                    SpinWheelColourSegment(
                        (size - defaultDimensions.frameWidth().value - defaultDimensions.selectorWidth().value),
                        state.pieCount,
                        state.rotation.value,
                        defaultColours.pieColors().value
                    )
                    SpinWheelContent(
                        spinSize = size - defaultDimensions.frameWidth().value - defaultDimensions.selectorWidth().value,
                        pieCount = state.pieCount,
                        rotationDegree = state.rotation.value,
                        textList
                    )
                    SpinWheelPointer() {
                        scope.launch { state.animate(coroutineScope) { pieIndex -> } }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp, end = 60.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Tap 'Spin' to play.",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro
                )
                Text(
                    text = stringResource(id = R.string.game_scratch_card_detail_instruction),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = font_sf_pro,
                    modifier = Modifier.padding(top=16.dp)
                )
            }
        }
    }
}

