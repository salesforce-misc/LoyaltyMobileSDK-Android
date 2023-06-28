package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.VOUCHER_EXPIRED
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.VOUCHER_ISSUED
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.VOUCHER_REDEEMED
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResponse

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VoucherView(voucher: VoucherResponse) {

    var voucherPopupState by remember { mutableStateOf(false) }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var clippedText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .width(165.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(bottom = 16.dp)
            .clickable {
                voucherPopupState = true
            }
    )

    {

        Box() {
            Image(
                painter = painterResource(id = R.drawable.promotionlist_image_placeholder),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .width(165.dp)
                    .height(92.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            GlideImage(
                model = voucher.voucherImageUrl,
                contentDescription = voucher.description,
                modifier = Modifier
                    .size(165.dp, 92.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            ) {
                it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        {
            voucher.voucherDefinition?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            //Part of Figma but Not part of MVP
            /*  Text(
                  text = "Details",
                  fontWeight = FontWeight.Normal,
                  color = LightBlack,
                  fontFamily = font_sf_pro,
                  textAlign = TextAlign.Start,
                  fontSize = 12.sp,
                  modifier = Modifier
                      .fillMaxWidth()
              )*/

            //Part of Figma but Not part of MVP
            /*  Text(
                  buildAnnotatedString {
                      withStyle(
                          style = SpanStyle()
                      ) {
                          append("Balance: ")
                      }
                      withStyle(
                          style = SpanStyle(fontWeight = FontWeight.Bold)
                      ) {
                          append("$11")
                      }
                  },
                  fontWeight = FontWeight.Normal,
                  color = LightBlack,
                  fontFamily = font_sf_pro,
                  textAlign = TextAlign.Start,
                  fontSize = 12.sp,
                  modifier = Modifier
                      .fillMaxWidth()

              )*/
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append(stringResource(id = R.string.text_valid_till))
                    }
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(voucher.expirationDate?.let { Common.formatPromotionDate(it) })
                    }
                },
                fontWeight = FontWeight.Normal,
                color = LightBlack,
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()

            )


            Spacer(modifier = Modifier.height(12.dp))

            if (voucher.status == VOUCHER_EXPIRED) {
                Text(
                    text = stringResource(id = R.string.voucher_text_expired),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else if (voucher.status == VOUCHER_REDEEMED) {
                Text(
                    text = stringResource(id = R.string.voucher_text_redeemed),
                    color = Color.Black,
                    fontFamily = font_sf_pro,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else if (voucher.status == VOUCHER_ISSUED) {

                Box(modifier = Modifier.width(250.dp))
                {
                    Image(
                        painter = painterResource(id = R.drawable.voucher_frame),
                        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                        modifier = Modifier
                            .height(32.dp)
                            .width(200.dp),
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
                                    Toast.makeText(context, voucherCopyText, Toast.LENGTH_SHORT).show()

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
                                modifier = Modifier.align(CenterVertically).padding(start = 5.dp)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.copy_icon),
                            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                            modifier = Modifier
                                .height(11.dp).align(CenterVertically).padding(end = 5.dp),
                            contentScale = ContentScale.Fit
                        )

                    }

                }
            }

        }
    }
    if (voucherPopupState) {
        VoucherPopup(
            voucher,
            closePopup = {
                voucherPopupState = false
            }
        )
    }
}