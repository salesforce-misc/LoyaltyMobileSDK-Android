package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerFrameColour

class SpinnerConfiguration {

    companion object {
        //preferences
        val WHEEL_SIZE = 280.dp
        val WHEEL_FRAME_WIDTH = 4.dp
        val WHEEL_FRAME_COLOUR = SpinnerFrameColour
        val WHEEL_BORDER_WIDTH = 12.dp
        val WHEEL_SEGMENT_BACKGROUND = Color.White //same colour will apply on border
        const val START_DEGREE = 0f
        //spinner default loading position degree 30f represents when spinner will load it will be at 30 degree
        const val ROTATION_PER_SECOND = 3f
        const val PADDING_ANGLE_BETWEEN_WHEEL_SEGMENT = 2f //padding between two segment of wheel
        const val ROTATION_DURATION = 3000 // duration of wheel rotate after API respond
        const val INITIAL_ROTATION_DURATION = 20000 // duration of wheel rotate till API respond
        const val WHEEL_TEXT_INITIAL_ROTATION = 0f // we can configure the orientation of text
        // inside wheel based on this angle we can make wheel content staright or with angle
    }
}