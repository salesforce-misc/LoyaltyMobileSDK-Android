package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.ReceiptScanningBottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_TABLE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ROW_STORE_DETAILS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

@Composable
fun ShowScannedReceiptScreen(
    navHostController: NavHostController,
    closePopup: () -> Unit,
    openCongratsPopup: (popupStatus: ReceiptScanningBottomSheetType) -> Unit
) {
    var congPopupState by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth()
            .background(MyProfileScreenBG, RoundedCornerShape(22.dp))
            .padding(16.dp)
            .testTag(TEST_TAG_RECEIPT_TABLE_SCREEN),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(R.string.field_receipt_number) + " " + "2323",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.SemiBold,
                color = LighterBlack,
            )
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TEST_TAG_ROW_STORE_DETAILS)
//                        .padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.field_store) + " " + "Store Name",
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier.weight(0.5f)
            )

            Text(
                text = stringResource(R.string.field_date_colon) + " " +"13-07-2023",
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier.weight(0.5f)
            )
        }
        ReceiptDetailTable()

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
                    congPopupState = true
//                        navHostController.navigate(MoreScreens.ScannedCongratsScreen.route)
                },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.button_submit_receipt),
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
                text = stringResource(id = R.string.button_try_again),
                fontFamily = font_sf_pro,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 3.dp)
                    .testTag(
                        TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT
                    )
                    .clickable {
                        closePopup()
                        navHostController.popBackStack(
                            MoreScreens.CaptureImageScreen.route,
                            false
                        )
                    },
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = LighterBlack,
                fontWeight = FontWeight.Normal,

                )
        }
    }
    if (congPopupState) {
        openCongratsPopup(ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS)
    }
}

/*@Preview
@Composable
fun ScannedPreview(){
    ShowScannedReceiptPopup()
}*/
