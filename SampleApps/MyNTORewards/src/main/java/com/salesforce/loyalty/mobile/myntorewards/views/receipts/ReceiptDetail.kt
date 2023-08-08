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

@Composable
fun ReceiptDetail( closePopup: () -> Unit) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = {closePopup()  },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
    ) {
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .background(MyProfileScreenBG, RoundedCornerShape(22.dp))
                    .padding(16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Receipt Number",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                        )
                        Text(
                            text = "13-07-2023",
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
                        Image(
                            painter = painterResource(id = R.drawable.close_popup_icon),
                            contentDescription = stringResource(R.string.cd_close_popup),
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .padding(3.dp).clickable{
                                    closePopup()
                                })
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.receipt_dummy),
                    contentDescription = "image description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(340.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.download_option),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = LighterBlack,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                    )
                )
            }
        }
    }
}