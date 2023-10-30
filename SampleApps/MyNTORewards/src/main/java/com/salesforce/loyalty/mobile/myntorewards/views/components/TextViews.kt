package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

@Composable
fun HeaderText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = font_sf_pro,
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(start = 32.dp, end = 32.dp)
    )
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = font_sf_pro,
        color = Color.Black,
        fontSize = 16.sp,
        modifier = modifier.padding(start = 32.dp, end = 32.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HeaderTextPreview() {
    HeaderText(text = stringResource(id = R.string.scanning_congrats))
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BodyTextPreview() {
    BodyText(text = stringResource(id = R.string.scanning_congrats_subtitle, "10"))
}