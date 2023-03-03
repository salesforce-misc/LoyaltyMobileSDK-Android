package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_EMAIL_ID
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_PROGRAM_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.PopupState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get


@Composable
fun EnrollmentCongratulationsPopup(
    navController: NavController,
    openPopup: (popupStatus: PopupState) -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 700),
        onDismissRequest = { openPopup(PopupState.POPUP_NONE) },
        properties = PopupProperties(focusable = true)
    ) {
        EnrollmentCongratulationsView(navController) {
            openPopup(it)
        }
    }
}

@Composable
fun EnrollmentCongratulationsView(
    navController: NavController,
    openPopup: (popupStatus: PopupState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {

            Image(
                painter = painterResource(id = R.drawable.congratulations),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Spacer(modifier = Modifier.height(25.dp))
            Image(
                painter = painterResource(id = R.drawable.gift),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                modifier = Modifier.width(135.dp),
                contentScale = ContentScale.FillWidth,

                )
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            var programName = PrefHelper.customPrefs(LocalContext.current)
                .get(KEY_PROGRAM_NAME, "")
            var emailID = PrefHelper.customPrefs(LocalContext.current)
                .get(KEY_EMAIL_ID, "")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.text_welcome_onboard),
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.onboard_text_msg_part_1) + programName + stringResource(
                    id = R.string.onboard_text_msg_part_2
                ),
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(77.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append(stringResource(id = R.string.text_we_sent_email))
                    }
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(emailID)
                    }
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append(stringResource(id = R.string.text_with_more_details))
                    }
                }, fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 59.dp, end = 59.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(92.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(), onClick = {
                    openPopup(PopupState.POPUP_NONE)
                    navController.navigate(Screen.HomeScreen.route)
                },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.shop_now_text),
                    fontFamily = font_sf_pro,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}