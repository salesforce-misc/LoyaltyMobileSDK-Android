package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
fun UserInfoRow()
{
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_pic),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(start = 16.dp),
            contentScale = ContentScale.FillWidth
        )
        Column( modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(start = 5.dp)
        ) {

            Text(
                text = "Julia Green",
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Text(
                text = "24252627",
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
        Column( modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.edit_icon),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier= Modifier.width(42.dp),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}