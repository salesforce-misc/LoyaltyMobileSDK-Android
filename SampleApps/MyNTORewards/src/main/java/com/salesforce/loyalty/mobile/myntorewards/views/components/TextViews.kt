package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

/**
 * This file has all Text Views related Composables with different shapes/styles.
 */
@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier) {
    CommonText(text = text, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = modifier)
}

@Composable
fun BodyText(text: String, modifier: Modifier = Modifier) {
    CommonText(text = text, fontSize = 16.sp, modifier = modifier)
}

@Composable
private fun CommonText(
    text: String,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = font_sf_pro,
    color: Color = Color.Black,
    fontSize: TextUnit = 16.sp,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Text(
        text = text,
        fontFamily = fontFamily,
        color = color,
        fontSize = fontSize,
        modifier = modifier,
        textAlign = textAlign,
        fontWeight = fontWeight
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