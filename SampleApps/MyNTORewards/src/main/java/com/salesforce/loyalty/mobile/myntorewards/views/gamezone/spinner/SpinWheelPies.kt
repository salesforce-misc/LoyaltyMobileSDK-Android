package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.util.Log
import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

@Composable
fun SpinWheelPies(
    modifier: Modifier = Modifier,
    spinSize: Dp,
    @IntRange(from = 2, to = 16) pieCount: Int,
    pieColors: List<Color>,
    rotationDegree: Float,
    onClick: () -> Unit,
    sizeWheel: Dp,
    frameWidth: Dp,
    selectorWidth: Dp,
    state: SpinWheelState,
    wheelData: MutableList<String>,
) {
    val pieAngle = 360f / pieCount
    val startAngleOffset = 270
    Box(modifier = modifier.padding(10.dp), contentAlignment = Alignment.Center) {

        SpinWheelColourSegment(spinSize,pieCount, pieAngle, rotationDegree, startAngleOffset, pieColors){
            onClick()
        }
    SpinWheelContent(
                modifier = modifier.padding(10.dp),
                spinSize = sizeWheel - frameWidth - selectorWidth,
                pieCount = state.pieCount,
                rotationDegree = state.rotation.value
            ) { i ->
        Log.d("Akash p",  ""+i)
        Text(
            text = wheelData[i],
            fontWeight = FontWeight.SemiBold,
            fontFamily = font_sf_pro,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
            }
        }
    }

@Composable
fun SpinWheelColourSegment(
    spinSize: Dp,
    pieCount: Int,
    pieAngle: Float,
    rotationDegree: Float,
    startAngleOffset: Int,
    pieColors: List<Color>,
    onClick: () -> Unit,
)
{
    Canvas(
        modifier = Modifier
            .size(spinSize)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ){

        val canvasWidth = size.width
        val canvasHeight = size.height
        for(i in 0 until pieCount){
            val startAngle = pieAngle * i + rotationDegree + startAngleOffset
            val nextColor = pieColors.getOrElse(i) { Color.LightGray }
            drawArc(
                color = nextColor,
                startAngle = startAngle,
                sweepAngle = pieAngle-2,
                useCenter = true,
                size = Size(canvasWidth, canvasHeight),
            )
        }
    }
}