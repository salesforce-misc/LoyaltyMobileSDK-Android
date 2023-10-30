package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R

@Composable
fun ImageComponent(
    drawableId: Int,
    contentDescription: String,
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