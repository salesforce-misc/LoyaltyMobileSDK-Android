package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ReceiptListScreenPopupState

@Composable
fun ReceiptDetail(closePopup: (ReceiptListScreenPopupState) -> Unit) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = { closePopup(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN) },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(MyProfileScreenBG, RoundedCornerShape(25.dp))
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                /*.verticalScroll(
                    rememberScrollState()
                )*/,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.close_popup_icon),
                    contentDescription = stringResource(R.string.cd_close_popup),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(end = 3.dp, bottom = 8.dp)
                        .align(Alignment.End)
                        .clickable {
                            closePopup(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN)
                        })

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(0.6f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.field_receipt_number) + " " + "12345",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                        )
                        Text(
                            text = stringResource(R.string.field_date) + " " + "13-07-2023",
                            fontFamily = font_sf_pro,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                        )

                    }
                    Column(
                        modifier = Modifier.weight(0.3f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "INR 32392",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.End,
                            fontSize = 13.sp,
                            modifier = Modifier
                        )
                        Text(
                            text = "434" + " Points",
                            color = Color.Black,
                            textAlign = TextAlign.End,
                            fontSize = 13.sp,
                            modifier = Modifier
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    ReceiptDetailTable()
                }
            }
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.manual_review_option),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = LighterBlack,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.clickable {
                        closePopup(ReceiptListScreenPopupState.MANUAL_REVIEW)
                    }
                )
                Text(
                    text = stringResource(id = R.string.download_option),
                    modifier = Modifier.padding(top = 16.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight(400),
                        color = LighterBlack,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}