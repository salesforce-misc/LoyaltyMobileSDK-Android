package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ReceiptListScreenPopupState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

@Composable
fun ManualReview( closePopup: (ReceiptListScreenPopupState) -> Unit) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = {closePopup(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN) },
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
                verticalArrangement = Arrangement.Top
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth().padding(end=16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.close_popup_icon),
                        contentDescription = stringResource(R.string.cd_close_popup),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .padding(3.dp).clickable{
                                closePopup(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN)
                            }.align(Alignment.End))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Submit for Manual Review",
                    fontWeight = FontWeight.SemiBold,
                    color = LighterBlack,
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(start = 8.dp))

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Receipt Number",
                            fontWeight = FontWeight.SemiBold,
                            color = LighterBlack,
                            fontFamily = font_sf_pro,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                        )

                        Text(
                            text = "13-07-2023",
                            fontFamily = font_sf_pro,
                            color = LighterBlack,
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
                            fontWeight = FontWeight.SemiBold,
                            color = LighterBlack,
                            fontFamily = font_sf_pro,
                            textAlign = TextAlign.End,
                            fontSize = 13.sp,
                        )
                        Text(
                            text = "434 Points",
                            fontFamily = font_sf_pro,
                            color = LighterBlack,
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Comments",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Normal,
                        color = LighterBlack,
                        textAlign = TextAlign.Start,
                    ),
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                var reviewText by remember { mutableStateOf(TextFieldValue("")) }
                OutlinedTextField(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color(0xFFA0A0A0), shape = RoundedCornerShape(size = 8.dp))
                    .fillMaxWidth().height(120.dp)
                    .background(color = Color(0xFFFAFCFF), shape = RoundedCornerShape(size = 8.dp)),

                    shape = RoundedCornerShape(8.dp),
                    value = reviewText,
                    onValueChange = {reviewText = it},
                    keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                    maxLines = 5,
                    textStyle = MaterialTheme.typography.caption
                )

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


                            closePopup(ReceiptListScreenPopupState.RECEIPT_LIST_SCREEN)// temporary. API call will be triggered.
                        },
                        colors = ButtonDefaults.buttonColors(VibrantPurple40),
                        shape = RoundedCornerShape(100.dp)

                    ) {
                        Text(
                            text = "Submit for Manual Review",
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
                        text = "Back",
                        fontFamily = font_sf_pro,
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 3.dp)
                            .testTag(
                                TestTags.TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT
                            )
                            .clickable {
                                closePopup(ReceiptListScreenPopupState.RECEIPT_DETAIL)

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
}