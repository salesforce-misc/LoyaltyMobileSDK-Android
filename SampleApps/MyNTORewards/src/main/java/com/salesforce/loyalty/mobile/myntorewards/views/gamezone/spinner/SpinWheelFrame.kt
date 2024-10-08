package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_FRAME
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_FRAME_COLOUR
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_SEGMENT_BACKGROUND


@Composable
fun SpinWheelFrame(frameSize: Dp, frameWidth: Dp) {
    Canvas(
        modifier = Modifier
            .size(frameSize).testTag(TEST_TAG_SPIN_WHEEL_FRAME)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawArc(
            color = WHEEL_FRAME_COLOUR,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
            style = Stroke(frameWidth.toPx()),
            size = Size(canvasWidth, canvasHeight)
        )
    }
}

@Composable
fun SpinWheelCircle(spinnerBackgroundSize: Dp) {
    Canvas(modifier = Modifier.size(spinnerBackgroundSize).testTag(TestTags.TEST_TAG_SPIN_WHEEL_CIRCLE)) {
        drawCircle(
            color = WHEEL_SEGMENT_BACKGROUND,
        )
    }
}