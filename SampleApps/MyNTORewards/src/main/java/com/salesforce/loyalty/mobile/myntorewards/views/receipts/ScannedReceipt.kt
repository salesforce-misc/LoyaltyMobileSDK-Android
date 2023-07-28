package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

@Composable
fun ShowScannedReceiptScreen(navHostController: NavHostController, closePopup: () -> Unit) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = {closePopup() },
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth()
                .background(MyProfileScreenBG, RoundedCornerShape(22.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = stringResource(R.string.label_receipt_number) + " " + "2323",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = font_sf_pro,
                    fontWeight = FontWeight(600),
                    color = LighterBlack,
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
//                        .padding(16.dp)
            ) {

                Text(
                    text = "Store: Store Name",
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(0.5f)
                )

                Text(
                    text = "Receipt Date: 13-07-2023",
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
                        navHostController.navigate(MoreScreens.ScannedCongratsScreen.route)
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
                        .clickable {
                            closePopup()
                            navHostController.popBackStack(
                                MoreScreens.CaptureImageScreen.route,
                                false
                            )
                        },
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color(0xFF181818),
                    fontWeight = FontWeight(400),

                    )
            }
        }
    }
}

/*@Preview
@Composable
fun ScannedPreview(){
    ShowScannedReceiptPopup()
}*/
