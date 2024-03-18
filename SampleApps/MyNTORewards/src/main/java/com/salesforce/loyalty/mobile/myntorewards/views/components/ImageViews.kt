package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R

/**
 * This file has all Image Views related Composables with different shapes/styles.
 */
@Composable
fun ImageComponent(
    drawableId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth
) {
    Image(
        painter = painterResource(drawableId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        alignment = Alignment.BottomCenter
    )
}

@Composable
fun RoundedIconButton(
    onClick: () -> Unit,
    vector: ImageVector = Icons.Default.Close,
    contentDescription: String = stringResource(R.string.cd_close_popup),
    tintColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier.padding(8.dp).size(36.dp).background(Color.White, CircleShape).clip(CircleShape)) {
        Icon(imageVector = vector, contentDescription = contentDescription, tint = tintColor)
    }
}

@Preview
@Composable
fun CircularImagePreview() {
    ImageComponent(drawableId = R.drawable.game_no_voucher_icon, "")
}

@Preview
@Composable
fun FullImagePreview() {
    ImageComponent(drawableId = R.drawable.congratulations, "", modifier = Modifier.fillMaxWidth())
}