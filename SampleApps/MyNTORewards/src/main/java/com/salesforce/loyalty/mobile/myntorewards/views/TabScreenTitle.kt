package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.MY_PROFILE_FULL_SCREEN_HEADER

@Composable
fun ScreenTabHeader() {
    Text(
        text = stringResource(id = R.string.screen_title_my_profiles),
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontFamily = font_sf_pro,
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth()
            .testTag(MY_PROFILE_FULL_SCREEN_HEADER)
    )
}
