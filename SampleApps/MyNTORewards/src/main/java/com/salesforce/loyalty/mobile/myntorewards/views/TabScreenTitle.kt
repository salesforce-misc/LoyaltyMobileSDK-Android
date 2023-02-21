package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R

@Composable
fun ScreenTabHeader() {
    Text(
        text = stringResource(id = R.string.screen_title_my_profiles),
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth()
    )
}
