package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SaffronColor
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SaffronColorLight

fun Modifier.dashedBorder(
    strokeWidth: Dp = 1.dp,
    color: Color = SaffronColor,
    solidColor: Color = SaffronColorLight,
    cornerRadiusDp: Dp = 8.dp,
    solidCornerRadiusDp: Dp = 10.dp
) = composed(
    factory = {
        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidth.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusDp.toPx())
                    )
                    drawRoundRect(
                        color = solidColor,
                        cornerRadius = CornerRadius(solidCornerRadiusDp.toPx())
                    )
                }
            }
        )
    }
)