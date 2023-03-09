package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberProfileResponse


@Composable
fun QRCodePopup(membershipProfile: MemberProfileResponse?, closePopup: () -> Unit) {

    val membershipNumber =
        PrefHelper.customPrefs(LocalContext.current)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""
    val firstName = (membershipProfile?.associatedContact?.firstName) ?: ""
    val lastName = (membershipProfile?.associatedContact?.lastName) ?: ""
    val userName = "$firstName $lastName"
    // openPopup(true)
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .background(MyProfileScreenBG, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        QRPopupHeader()
        {
            closePopup()
        }
        QRCodeBox(membershipNumber, userName)
        Text(
            text = stringResource(id = R.string.scan_the_qr_code_text),
            fontFamily = font_sf_pro,
            color = LightBlack,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        QRPopupCloseButton {
            closePopup()
        }

        Spacer(modifier = Modifier.height(55.dp))

    }
}

@Composable
fun QRPopupHeader(closePopup: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 18.dp)
    )

    {
        Text(
            text = stringResource(id = R.string.my_membership_code),
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(start = 16.dp),
            textAlign = TextAlign.Start,
        )

        Image(
            painter = painterResource(id = R.drawable.close_popup_icon),
            contentDescription = stringResource(id = R.string.cd_close_popup),
            modifier = Modifier
                .width(16.dp)
                .height(16.dp)
                .clickable {
                    closePopup()
                },
            contentScale = ContentScale.FillWidth,
        )
    }

}


@Composable
fun QRCodeBox(membershipNumber: String, userName: String) {
    Column(modifier = Modifier.padding(start = 32.dp, end = 32.dp)) {

        Box {

            Column(verticalArrangement = Arrangement.Top) {
                Spacer(modifier = Modifier.height(22.dp))
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Spacer(modifier = Modifier.height(37.dp))

                    Text(
                        text = userName,
                        fontFamily = font_sf_pro,
                        color = LightBlack,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Text(
                        text = stringResource(id = R.string.membership_qr_code),
                        fontFamily = font_sf_pro,
                        color = LightBlack,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(modifier = Modifier.height(42.dp))
                    QRCode(membershipNumber, 120, 120)


                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(id = R.string.my_membership_number) + membershipNumber,
                        fontFamily = font_sf_pro,
                        color = LightBlack,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.height(52.dp))

                }
            }
            Image(
                painter = painterResource(id = R.drawable.user_pic),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .align(TopCenter),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
fun QRPopupCloseButton(closePopup: () -> Unit) {

    Column(modifier = Modifier.padding(start = 32.dp, end = 32.dp)) {
        Button(
            modifier = Modifier
                .fillMaxWidth(), onClick = {
                closePopup()
            },
            colors = ButtonDefaults.buttonColors(VibrantPurple40),
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(
                text = stringResource(id = R.string.close_text),
                fontFamily = font_sf_pro,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
        }
    }
}

