package com.salesforce.loyalty.mobile.myntorewards.views.receipts

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CONGRATULATIONS_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

@Composable
fun CongratulationsPopup(
    totalPoints: String?,
    closePopup: () -> Unit,
    scanAnotherReceipt: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(22.dp))
            .testTag(TEST_TAG_CONGRATULATIONS_SCREEN),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.congratulations),
                contentDescription = stringResource(R.string.scanning_congrats_background),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Image(
                painter = painterResource(id = R.drawable.gift),
                contentDescription = stringResource(R.string.scanning_congrats_gift_icon),
                modifier = Modifier.width(135.dp),
                contentScale = ContentScale.FillWidth,

                )
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                text = stringResource(id = R.string.scanning_congrats),
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (totalPoints != null) {
                    stringResource(id = R.string.scanning_congrats_subtitle, totalPoints)
                } else {
                    stringResource(id = R.string.scanning_receipt_subtitle_no_points)
                },
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center
            )

//            Spacer(modifier = Modifier.height(92.dp))
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(), onClick = {
                        closePopup()
//                        navController.popBackStack(MoreScreens.ReceiptListScreen.route, false)
                    },
                    colors = ButtonDefaults.buttonColors(VibrantPurple40),
                    shape = RoundedCornerShape(100.dp)

                ) {
                    Text(
                        text = stringResource(id = R.string.scanning_done),
                        fontFamily = font_sf_pro,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 3.dp, bottom = 3.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.scan_another_receipt),
                    fontFamily = font_sf_pro,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 3.dp)
                        .clickable {
                            scanAnotherReceipt()
//                            navController.popBackStack(MoreScreens.CaptureImageScreen.route, false)
                        },
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = LighterBlack,
                    fontWeight = FontWeight.Normal,

                    )
            }
        }
    }

}