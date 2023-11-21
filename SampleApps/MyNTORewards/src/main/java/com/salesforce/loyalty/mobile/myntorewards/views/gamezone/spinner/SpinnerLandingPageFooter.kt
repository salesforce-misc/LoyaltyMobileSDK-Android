package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

@Composable
fun SpinWheelLandingPageFooter() {
    Spacer(modifier = Modifier.height(24.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.tap_spin_to_play),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro
        )
        Text(
            text = stringResource(id = R.string.game_scratch_card_detail_instruction),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = font_sf_pro,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}