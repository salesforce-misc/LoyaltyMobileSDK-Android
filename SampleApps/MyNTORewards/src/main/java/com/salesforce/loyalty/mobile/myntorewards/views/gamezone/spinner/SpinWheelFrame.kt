package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerFrameColour


@Composable
fun SpinWheelFrame(frameSize: Dp, frameWidth: Dp)
{
    Canvas(
        modifier = Modifier
            .size(frameSize)
    ){
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawArc(
            color = SpinnerFrameColour,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
            style = Stroke(frameWidth.toPx()),
            size = Size(canvasWidth, canvasHeight)
        )
    }
}
@Composable
fun SpinWheelCircle(frameSize: Dp, frameWidth: Dp)
{
    Canvas(
        modifier = Modifier
            .size(frameSize-10.dp)
    ){
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = Color.White,
            radius = 400f,
        )
    }
}