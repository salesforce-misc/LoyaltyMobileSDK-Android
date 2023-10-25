package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelPointer
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Wheel(loyaltyAPIManager: LoyaltyAPIManager, gamesList: MutableList<String>, colourList: MutableList<Color>)
{
    val state = rememberSpinWheelState(loyaltyAPIManager, gamesList.size)
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
        Box(
            contentAlignment = Alignment.Center
        ) {
            SpinWheelFrame(size, defaultDimensions.frameWidth().value)

            SpinWheelColourSegment((size - defaultDimensions.frameWidth().value - defaultDimensions.selectorWidth().value), state.pieCount,  state.rotation.value, defaultColours.pieColors().value)
            {
                scope.launch { state.animate(coroutineScope) {pieIndex -> } }
            }
            SpinWheelContent(
                spinSize = size - defaultDimensions.frameWidth().value - defaultDimensions.selectorWidth().value,
                pieCount = state.pieCount,
                rotationDegree = state.rotation.value,
                textList
            )
            SpinWheelPointer()
        }
    }
}

