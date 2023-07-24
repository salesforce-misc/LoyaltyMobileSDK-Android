package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.delay

@Composable
fun ScanningProgress(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyProfileScreenBG)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .background(MyProfileScreenBG)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.3f)
            )

            Text(
                text = stringResource(id = R.string.scan_progress_indicator),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = font_sf_pro,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.32.sp,
                )
            )

            Text(
                text = stringResource(id = R.string.scan_progress_subheader),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = font_sf_pro,
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                )
            )
        }
        Column(
            modifier = Modifier
                .weight(0.1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(id = R.string.btn_cancel),
                fontFamily = font_sf_pro,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 3.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF181818),
                fontWeight = FontWeight(400),

                )
        }
        LaunchedEffect(true) {
            // Added delay for testing purpose. Need to remove it once we call the API to scan the receipt.
            delay(5000)
            navHostController.navigate(MoreScreens.ScannedReceiptScreen.route)
        }
    }
}