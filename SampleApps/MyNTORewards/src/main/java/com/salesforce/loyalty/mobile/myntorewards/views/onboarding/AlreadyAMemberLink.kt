package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

@Composable
fun AlreadyAMemberButton(openLoginPopup: () -> Unit) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    fontFamily = font_sf_pro,
                    fontSize = 16.sp
                )
            ) {
                append(stringResource(id = R.string.existing_member_text))
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = font_sf_pro,
                    fontSize = 16.sp
                )
            ) {
                append(stringResource(id = R.string.login_text))
            }
        }, modifier = Modifier
            .fillMaxWidth(1f)
            .clickable {
                openLoginPopup()
            },
        textAlign = TextAlign.Center

    )
}