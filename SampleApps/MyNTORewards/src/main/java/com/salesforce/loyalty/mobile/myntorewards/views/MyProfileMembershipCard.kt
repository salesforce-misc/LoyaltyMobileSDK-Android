package com.salesforce.loyalty.mobile.myntorewards.views

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MembershipTierBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipProfileViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OnboardingScreenViewModel
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberCurrency
import kotlin.math.floor

@Composable
fun ProfileCard() {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .background(TextPurpoleLightBG)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(4.dp))
        ) {
            CardBackground()
            CardContent()
        }
    }
}

@Composable
fun CardBackground() {
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
fun CardContent() {
    Column(
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .padding(start = 32.dp, end = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {

        val model: MembershipProfileViewModel = viewModel()  //fetching reference of viewmodel
        val membershipProfile by model.membershipProfileLiveData.observeAsState() // collecting livedata as state
        val context: Context = LocalContext.current
        model.getMemberProfile(context)
        //calling member benefit
        model.memberBenefitAPI(context)

        //loginStatus state being change to Success after token fetch
        Spacer(modifier = Modifier.height(16.dp))
        membershipProfile?.memberTiers?.get(0)?.loyaltyMemberTierName?.let { MembershipTierRow(it) }


        membershipProfile?.memberCurrencies?.get(0)?.let { RewardPointsAndExpiry(it) }
        Spacer(modifier = Modifier.height(24.dp))

        membershipProfile?.memberCurrencies?.get(0)?.loyaltyMemberCurrencyName.let {
            if (it != null) {
                QRCodeRow(it)
            }
        }
    }
}

@Composable
fun MembershipTierRow(tierName: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = tierName,
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
fun RewardPointsAndExpiry(memberCurrency: MemberCurrency) {

    val pointBalance: String = memberCurrency.pointsBalance.toString()
    Text(
        text = pointBalance,
        fontFamily = font_sf_pro,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 32.sp
    )

    val expirablePoints: String = memberCurrency.expirablePoints.toString()
    val expirationDate: String = memberCurrency.lastExpirationProcessRunDate.toString()
    Text(
        text = "$expirablePoints points expiring on $expirationDate",
        fontFamily = font_sf_pro,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )
}

@Composable
fun QRCodeRow(loyaltyMemberCurrencyName: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = loyaltyMemberCurrencyName,
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
