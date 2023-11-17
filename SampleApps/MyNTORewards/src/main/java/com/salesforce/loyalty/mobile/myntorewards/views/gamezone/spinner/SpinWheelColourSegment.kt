package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.PADDING_ANGLE_BETWEEN_WHEEL_SEGMENT

@Composable
fun SpinWheelColourSegment(
    spinSize: Dp,
    pieCount: Int,
    rotationDegree: Float,
    pieColors: List<Color>,

    ) {
    val pieAngle = 360f / pieCount
    val startAngleOffset = 270

    Canvas(
        modifier = Modifier
            .size(spinSize)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height
        for (i in 0 until pieCount) {
            val startAngle = pieAngle * i + rotationDegree + startAngleOffset
            val nextColor = pieColors.getOrElse(i) { Color.LightGray }
            drawArc(
                color = nextColor,
                startAngle = startAngle,
                sweepAngle = pieAngle - PADDING_ANGLE_BETWEEN_WHEEL_SEGMENT,
                useCenter = true,
                size = Size(canvasWidth, canvasHeight),
            )
        }
    }
}