package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.GameNameIDDataModel
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.WHEEL_TEXT_INITIAL_ROTATION
import java.lang.Math.cos
import java.lang.Math.sin

@Composable
internal fun SpinWheelContent(
    spinSize: Dp,
    @IntRange(from = 2, to = 16) pieCount: Int,
    rotationDegree: Float, // maintaining the degree of wheel has been rotated
    wheelData: MutableList<GameNameIDDataModel>,
) {
    val pieAngle = 360f / pieCount
    val startOffset = 180
    val radius = (spinSize.value / 2)
    val pieRadius = getPieRadius(pieCount, radius) //provided offset info for content
    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(spinSize),
        contentAlignment = Alignment.Center
    ) {
        for (pieIndex in 0 until pieCount) {
            //rotationDegree is maintaining the degree of wheel has been rotated  ,
            val startAngle = pieAngle * pieIndex + startOffset + rotationDegree + pieAngle / 2

            val offsetX = -(pieRadius * sin(Math.toRadians(startAngle.toDouble()))).toFloat()

            val offsetY = (pieRadius * cos(Math.toRadians(startAngle.toDouble()))).toFloat()

            val rotateAngle =
                WHEEL_TEXT_INITIAL_ROTATION + (pieAngle / 2) + (pieAngle * pieIndex) + rotationDegree
            Box(
                modifier = Modifier
                    .size(spinSize / 4) //sets the size of box based on different sizes
                    .offset(x = Dp(offsetX), y = Dp(offsetY)),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    modifier = Modifier
                        .rotate(rotateAngle)
                ) {
                    //this is for POC to show image can also be added other than text
                  /*  if (pieIndex == 2) {

                        Image(
                            painter = painterResource(id = R.drawable.phone_icon),
                            contentDescription = "game_back_button",
                            contentScale = ContentScale.Fit
                        )
                    }
*/
                    Text(
                        text = wheelData[pieIndex].game,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = font_sf_pro,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,)

                }
            }
        }
    }
}

fun getPieRadius(pieCount: Int, radius: Float): Float {
    return when (pieCount) {
        2 -> radius / 2f
        3 -> radius / 1.8f
        4 -> radius / 1.8f
        5 -> radius / 1.6f
        6 -> radius / 1.6f
        7 -> radius / 1.4f
        8 -> radius / 1.4f
        else -> radius / 1.4f
    }
}