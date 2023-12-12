package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TierColourWhite
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

/**
 * This file has all buttons related Composables with different styles.
 */
@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    textContent: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(100.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(VibrantPurple40),
    border: BorderStroke? = null,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = colors,
        shape = shape,
        border = border,
        enabled = enabled
    ) {
        ButtonText(textContent, Color.White)
    }
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    textContent: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(100.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(TierColourWhite),
    border: BorderStroke = BorderStroke(2.dp, VibrantPurple40),
) {
    OutlinedButton(
        modifier = modifier,
        onClick = { onClick() },
        colors = colors,
        shape = shape,
        border = border,
        enabled = enabled
    ) {
        ButtonText(textContent, VibrantPurple40)
    }
}

@Composable
private fun ButtonText(textContent: String, textColor: Color) {
    Text(
        text = textContent,
        fontFamily = font_sf_pro,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = textColor,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .padding(top = 3.dp, bottom = 3.dp)
    )
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(textContent = stringResource(id = R.string.scanning_done), onClick = {})
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    SecondaryButton(textContent = stringResource(id = R.string.btn_cancel), onClick = {})
}
