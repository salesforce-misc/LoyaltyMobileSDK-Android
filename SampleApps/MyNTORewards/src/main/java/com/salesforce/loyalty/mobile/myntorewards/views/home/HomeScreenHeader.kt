package com.salesforce.loyalty.mobile.myntorewards.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipProfileViewModel


@Composable
fun AppLogoAndSearchRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(VibrantPurple40),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.membership_card_logo),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .padding(start = 16.dp),
            contentScale = ContentScale.FillWidth
        )
        Image(
            painter = painterResource(id = R.drawable.research),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .padding(end = 16.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun UserNameAndRewardRow() {
    val model: MembershipProfileViewModel = viewModel()
    val membershipProfile by model.membershipProfileLiveData.observeAsState() // collecting livedata as state

    val firstName = (membershipProfile?.associatedContact?.firstName) ?: ""
    val lastName = (membershipProfile?.associatedContact?.lastName) ?: ""
    val userName = "$firstName $lastName"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightPurple)
            .padding(top = 13.dp, bottom = 13.dp),

        horizontalArrangement = Arrangement.SpaceBetween,

        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = "Welcome, Akash Agrawal !",
            fontWeight = FontWeight.Normal,
            fontFamily = font_sf_pro,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "17850 Points",
            fontWeight = FontWeight.Normal,
            fontFamily = font_sf_pro,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

