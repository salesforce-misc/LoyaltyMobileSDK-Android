package com.salesforce.loyalty.mobile.myntorewards.views.myprofile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.ColourPurpleQR
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BLUR_BG
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.NO_BLUR_BG
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.REWARD_CURRENCY_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.Assets
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_QR_CODE
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MembershipProfileViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyProfileViewStates
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberProfileResponse

@Composable
fun ProfileCard(profileModel: MembershipProfileViewModelInterface, blurBG: (Dp) -> Unit) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .background(TextPurpleLightBG)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(4.dp))
        ) {
            CardBackground()
            CardContent(profileModel){
                blurBG(it)
            }
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
fun CardContent(profileModel: MembershipProfileViewModelInterface, blurBG: (Dp) -> Unit) {
    Box() {
        var isInProgress by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .padding(start = 32.dp, end = 30.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {


            val membershipProfile by profileModel.membershipProfileLiveData.observeAsState() // collecting livedata as state
            val membershipProfileFetchStatus by profileModel.profileViewState.observeAsState() // collecting livedata as state
            val context: Context = LocalContext.current
            LaunchedEffect(key1 = true) {
                profileModel.loadProfile(context)
                isInProgress = true
            }

            when (membershipProfileFetchStatus) {
                MyProfileViewStates.MyProfileFetchSuccess -> {
                    isInProgress = false

                }
                MyProfileViewStates.MyProfileFetchInProgress -> {
                    isInProgress = true

                }
                MyProfileViewStates.MyProfileFetchFailure -> {
                    isInProgress = false
                }

                else -> {}
            }

            //loginStatus state being change to Success after token fetch
            if (!isInProgress) {
                Spacer(modifier = Modifier.height(16.dp))
                membershipProfile?.memberTiers?.get(0)?.loyaltyMemberTierName?.let {
                    MembershipTierRow(
                        it
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                membershipProfile?.memberCurrencies?.let {
                    RewardPointsAndExpiry(Common.getCurrencyPointBalance(it))
                }
                Spacer(modifier = Modifier.height(10.dp))

                QRCodeRow(membershipProfile){
                    blurBG(it)
                }
            }

        }
        if (isInProgress) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.Center),
                color = Color.White
            )
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
                    Assets.getTierColor(tierName.lowercase()), RoundedCornerShape(30.dp)
                )
                .padding(top = 3.dp, start = 16.dp, end = 16.dp, bottom = 3.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.membership_card_logo),
            contentDescription = "Profile Card Image Content Logo",
            modifier = Modifier.width(96.dp),
            contentScale = ContentScale.FillWidth

        )
    }
}

@Composable
fun RewardPointsAndExpiry(memberCurrencyPointBalance: Double?) {

    Text(
        text = memberCurrencyPointBalance.toString(),
        fontFamily = font_sf_pro,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 32.sp
    )

    //commented this code its part of UX but not part of current MVP
    /* val expirablePoints: String = memberCurrency.expirablePoints.toString()
     val expirationDate: String = memberCurrency.lastExpirationProcessRunDate.toString()
     Text(
         text = "$expirablePoints points expiring on $expirationDate",
         fontFamily = font_sf_pro,
         fontWeight = FontWeight.Bold,
         color = Color.White,
         textAlign = TextAlign.Center,
         fontSize = 12.sp
     )*/
}

@Composable
fun QRCodeRow(membershipProfile: MemberProfileResponse?, blurBG: (Dp) -> Unit) {

    val membershipID =
        membershipProfile?.loyaltyProgramMemberId ?: ""
    val loyaltyMemberCurrencyName = REWARD_CURRENCY_NAME

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var popupControlQRCOde by remember { mutableStateOf(false) }
        Text(
            text = loyaltyMemberCurrencyName,
            fontFamily = font_sf_pro,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Column(modifier = Modifier.clickable {
            popupControlQRCOde = true
            blurBG(BLUR_BG)
        }.testTag(TEST_TAG_QR_CODE)) {
            QRCode(value = membershipID, width = 46, height = 46, ColourPurpleQR)
        }

        if (popupControlQRCOde) {
            Popup(
                alignment = Alignment.Center,
                offset = IntOffset(0, 700),
                onDismissRequest = {
                    popupControlQRCOde = false
                    blurBG(NO_BLUR_BG) },
                properties = PopupProperties(focusable = true)
            ) {
                QRCodePopup(membershipProfile) {
                    popupControlQRCOde = false
                    blurBG(NO_BLUR_BG)
                }
            }
        }
    }
}