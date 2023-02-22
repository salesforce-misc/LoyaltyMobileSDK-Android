package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*

@Composable
fun TransactionCard() {
    Column(
        modifier = Modifier
            .background(MyProfileScreenBG)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        ProfileSubViewHeader(stringResource(id = R.string.my_transactions))
        {

        }

        ListItemTransaction()
        ListItemTransaction()
        ListItemTransaction()
    }
}

@Composable
fun ListItemTransaction() {
    Spacer(modifier = Modifier.height(12.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.weight(0.15f)) {
            Image(
                painter = painterResource(id = R.drawable.transaction_icon_1),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        Column(modifier = Modifier.weight(0.6f)) {
            Text(
                text = "Promotion Enrollment",
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
            )
            Text(
                text = "05’ June 2022",
                fontFamily = font_sf_pro,
                color = TextDarkGray,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
            )

        }
        Column(modifier = Modifier.weight(0.25f)) {
            Text(
                text = "+1500 Pts",
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Bold,
                color = TextGreen,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier
            )
        }
    }
}