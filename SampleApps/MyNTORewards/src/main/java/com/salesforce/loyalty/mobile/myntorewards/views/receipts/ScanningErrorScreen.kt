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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextDarkGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

@Composable
fun ScanningErrorPopup(
    errorString: Int,
    closePopup: () -> Unit,
    scanAnotherReceipt: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(22.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .weight(0.75f)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_astronaut),
                contentDescription = stringResource(R.string.error_cd),
                modifier = Modifier
                    .width(220.dp)
                    .height(220.dp),
                contentScale = ContentScale.FillWidth,

                )

            Text(
                text = stringResource(id = errorString),
                fontFamily = font_sf_pro,
                color = TextDarkGray,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .weight(0.25f)
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(), onClick = {
                    scanAnotherReceipt()
                },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.button_try_again),
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
                text = stringResource(id = R.string.button_home),
                fontFamily = font_sf_pro,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 3.dp)
                    .clickable {
                        closePopup()
                    },
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = LighterBlack,
                fontWeight = FontWeight.Normal,

                )
        }
    }
}