package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.delay

@Composable
fun ScanningProgress(
    navHostController: NavHostController,
    closePopup: () -> Unit,
    openScannedReceiptPopup: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = {closePopup() },
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        var detailsPopupState by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth()
                .background(
                    MyProfileScreenBG,
                    RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE)
                ),
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
            LaunchedEffect(true) {
                // Added delay for testing purpose. Need to remove it once we call the API to scan the receipt.
                delay(5000)
                detailsPopupState = true
            }
        }
        if(detailsPopupState){
            openScannedReceiptPopup()
        }
    }
}