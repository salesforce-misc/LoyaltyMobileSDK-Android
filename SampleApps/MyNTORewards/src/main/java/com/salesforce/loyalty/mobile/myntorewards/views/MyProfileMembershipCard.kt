package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MembershipTierBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

@Composable
fun ProfileCard()
{
    Card(shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .background(TextPurpoleLightBG)) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(4.dp))) {
            CardBackground()
            CardContent()
        }
    }
}
@Composable
fun CardBackground()
{
    Image(
        painter = painterResource(id = R.drawable.user_membership_card_bg),
        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )

}
@Composable
fun CardContent()
{
    Column(
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .padding(start = 32.dp, end = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        MembershipTierRow()
        Spacer(modifier = Modifier.height(24.dp))
        RewardPointsAndExpiry()
        QRCodeRow()
    }
}

@Composable
fun MembershipTierRow()
{
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
    Text(
        text = "Gold",
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        modifier = Modifier
            .background(
                MembershipTierBG, RoundedCornerShape(30.dp)
            )
            .padding(top = 3.dp, start = 16.dp, end = 16.dp, bottom = 3.dp)
    )
    Image(
        painter = painterResource(id = R.drawable.membership_card_logo),
        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
        modifier = Modifier.width(96.dp),
        contentScale = ContentScale.FillWidth

    )
}
}

@Composable
fun RewardPointsAndExpiry()
{
    Text(
        text = "17850",
        fontFamily = font_sf_pro,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 32.sp
    )

    Text(
        text = "100 points expiring on Oct 20 2022",
        fontFamily = font_sf_pro,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )
}

@Composable
fun QRCodeRow()
{
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Rewards Points",
            fontFamily = font_sf_pro,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Image(
            painter = painterResource(id = R.drawable.sample_qr_code),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .width(46.dp)
                .height(46.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}
