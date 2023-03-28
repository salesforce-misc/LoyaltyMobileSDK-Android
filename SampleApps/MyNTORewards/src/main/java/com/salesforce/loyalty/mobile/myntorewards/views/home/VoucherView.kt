package com.salesforce.loyalty.mobile.myntorewards.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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


    Column(
        modifier = Modifier
            .width(165.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(bottom = 16.dp)
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
            )

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
                        append("Valid till: ")
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
                Box()
                {
                    Image(
                        painter = painterResource(id = R.drawable.voucher_frame),
                        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                        modifier = Modifier
                            .height(32.dp)
                            .height(145.dp),
                        contentScale = ContentScale.FillWidth
                    )

                    Row(
                        modifier = Modifier
                            .height(32.dp)
                            .height(145.dp)
                    ) {

                        voucher.voucherCode?.let {
                            Text(
                                text = it,
                                fontWeight = FontWeight.Bold,
                                color = VoucherColourCode,
                                fontFamily = font_sf_pro,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }

                    }

                }
            }

        }
    }
}