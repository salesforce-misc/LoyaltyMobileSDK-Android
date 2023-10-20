package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
internal fun AnimatedSpinWheel(
    modifier: Modifier = Modifier,
    state: SpinWheelState,
    size: Dp,
    frameWidth: Dp,
    selectorWidth: Dp,
    pieColors: List<Color>,
    onClick: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    wheelData: MutableList<String>,
){
    LaunchedEffect(key1 = state.autoSpinDelay) {
        state.autoSpinDelay?.let {
            delay(it)
            state.spin(coroutineScope){

            }
        }
    }
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            SpinWheelFrame(size, frameWidth)
            SpinWheelPies(
                modifier = modifier.padding(10.dp),
                spinSize = size - frameWidth - selectorWidth,
                pieCount = state.pieCount,
                pieColors = pieColors,
                rotationDegree = state.rotation.value,
                onClick = onClick,
                size,
                frameWidth,
                selectorWidth,
                state,
                wheelData=wheelData
            )

            SpinWheelPointer()
    }
}

@Composable
fun SpinWheelPointer()
{
    Image(
        painter = painterResource(id = R.drawable.wheel_pointer),
        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
        modifier = Modifier
            .padding( bottom = 60.dp).testTag(TestTags.TEST_TAG_APP_LOGO_HOME_SCREEN),
        contentScale = ContentScale.FillWidth
    )
}