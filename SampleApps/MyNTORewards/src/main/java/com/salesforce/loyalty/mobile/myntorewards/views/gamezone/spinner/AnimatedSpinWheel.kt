package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
internal fun AnimatedSpinWheel(
    modifier: Modifier,
    state: SpinWheelState,
    size: Dp,
    frameWidth: Dp,
    selectorWidth: Dp,
    frameColor: Color,
    dividerColor: Color,
    selectorColor: Color,
    pieColors: List<Color>,
    onClick: () -> Unit,
    content: @Composable BoxScope.(pieIndex: Int) -> Unit
){
    LaunchedEffect(key1 = state.autoSpinDelay) {
        state.autoSpinDelay?.let {
            delay(it)
            state.spin()
        }
    }
    SpinWheelSelector(
        modifier = modifier.padding(10.dp),
        frameSize = size,
        pieCount = state.pieCount,
        selectorWidth = selectorWidth,
        selectorColor = selectorColor,
        rotationDegree = state.rotation.value
    ) {
        SpinWheelFrame(
            modifier = modifier.padding(10.dp),
            frameSize = size - selectorWidth,
            pieCount = state.pieCount,
            frameWidth = frameWidth,
            frameColor = frameColor,
            dividerColor =  dividerColor,
            rotationDegree = state.rotation.value,
            onClick = onClick,
        ) {
            SpinWheelPies(
                modifier = modifier.padding(10.dp),
                spinSize = size - frameWidth - selectorWidth,
                pieCount = state.pieCount,
                pieColors = pieColors,
                rotationDegree = state.rotation.value,
                onClick = onClick
            ){
                SpinWheelContent(
                    modifier = modifier.padding(10.dp),
                    spinSize = size - frameWidth - selectorWidth,
                    pieCount = state.pieCount,
                    rotationDegree = state.rotation.value
                ){
                    content(it)
                }
            }
        }
    }
}