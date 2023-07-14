package com.salesforce.loyalty.mobile.myntorewards.views.onboarding

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.BottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ENROLLMENT_CONGRATULATIONS
import com.salesforce.loyalty.mobile.sources.PrefHelper


@Composable
fun EnrollmentCongratulationsView(navController: NavController, closeSheet: () -> Unit, openPopup: (popupStatus: BottomSheetType) -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color.White, shape = RoundedCornerShape(16.dp)).testTag(TEST_TAG_ENROLLMENT_CONGRATULATIONS),
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

            val memberJson =
                PrefHelper.customPrefs(LocalContext.current)
                    .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)

            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)

            var programName = member?.loyaltyProgramName ?: ""

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
                text = stringResource(id = R.string.onboard_text_msg_part_3),
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 59.dp, end = 59.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(92.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(), onClick = {
                    openPopup(BottomSheetType.POPUP_LOGIN)
                },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.login_text),
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