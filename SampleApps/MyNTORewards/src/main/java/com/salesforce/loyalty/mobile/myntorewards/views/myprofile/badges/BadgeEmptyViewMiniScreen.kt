package com.salesforce.loyalty.mobile.myntorewards.views.myprofile.badges

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro




@Composable

fun BadgeErrorAndEmptyViewMiniScreen(imageIcon: Int, screenErrorOrEmptyText:String, textColour: Color = Color.Unspecified) {

    Column(verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(imageIcon),
            contentDescription = stringResource(R.string.cd_badge_error_empty_view_mini_screen),
            modifier = Modifier
                .width(80.81.dp)
                .height(82.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillWidth,
        )

        Text(
            text = screenErrorOrEmptyText,
            fontWeight = FontWeight.Normal,
            color = textColour,
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

    }
}
