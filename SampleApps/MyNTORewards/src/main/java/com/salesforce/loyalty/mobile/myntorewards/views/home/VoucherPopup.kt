package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.views.myprofile.QRCode
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResponse

@Composable
fun VoucherPopup(
    voucher: VoucherResponse,
    closePopup: () -> Unit,
) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = { },
        properties = PopupProperties(focusable = true),
    ) {


        VoucherPopupUI(
            voucher,
            closePopup = {
                closePopup()
            },
        )
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VoucherPopupUI(
    voucher: VoucherResponse,
    closePopup: () -> Unit,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var clippedText by remember { mutableStateOf("") }

    Column(
            modifier = Modifier
                .fillMaxHeight(0.98f)
                .background(Color.White, RoundedCornerShape(16.dp))
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )

        {

            Column{
                Box()
                {
                    Box() {
                        Image(
                            painter = painterResource(id = R.drawable.promotion_card_placeholder),
                            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        GlideImage(
                            model = voucher.voucherImageUrl,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),

                            contentScale = ContentScale.Crop
                        ) {
                            it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.End

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.close_popup_icon),
                            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .padding(3.dp)
                                .clickable {
                                    closePopup()
                                }

                        )
                    }

                }



                Spacer(modifier = Modifier.height(16.dp))
                voucher.voucherDefinition?.let{
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        color = LighterBlack,
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        fontFamily = font_sf_pro,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val discountPercent = voucher.discountPercent ?: 0
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle()
                        ) {
                            append("Discount: ")
                        }
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("$discountPercent%")
                        }
                    },
                    fontFamily = font_sf_pro,
                    color = LightBlack,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp

                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle()
                        ) {
                            append("Expiring on: ")
                        }
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append(voucher.expirationDate?.let { Common.formatPromotionDate(it) })
                        }
                    },
                    fontFamily = font_sf_pro,
                    color = LightBlack,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp

                )
            }
            Spacer(modifier = Modifier.height(44.dp))
            voucher.voucherCode?.let{
                QRCode(it, 110, 110)
            }

            Spacer(modifier = Modifier.height(9.dp))
            Box(modifier = Modifier.width(145.dp))
            {
                Image(
                    painter = painterResource(id = R.drawable.voucher_frame),
                    contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                    modifier = Modifier
                        .height(32.dp)
                        .width(145.dp),
                    contentScale = ContentScale.FillWidth
                )
                val context = LocalContext.current
                val voucherCopyText = stringResource(id = R.string.text_voucher_copy_copied)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .height(32.dp)
                        .width(200.dp)
                        .clickable {
                            voucher.voucherCode?.let {
                                clipboardManager.setText(AnnotatedString((it)))
                                Toast
                                    .makeText(context,voucherCopyText, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                ) {

                    voucher.voucherCode?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            color = VoucherColourCode,
                            fontFamily = font_sf_pro,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 5.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(id = R.drawable.copy_icon),
                            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                            modifier = Modifier
                                .height(11.dp)
                                .height(11.dp)
                                .align(Alignment.CenterVertically)
                                .padding(end = 5.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                }

            }
            Spacer(modifier = Modifier.height(120.dp))
            CloseButton()
            {
                closePopup()
            }
        }
}

@Composable
fun CloseButton(closePopup: () -> Unit) {

    Button(
        modifier = Modifier
            .width(300.dp), onClick = {
            closePopup()
            }
        ,
        colors = ButtonDefaults.buttonColors(VibrantPurple40),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = stringResource(id = R.string.close_text),
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 2.dp, bottom = 2.dp)
        )
    }
}
