package com.salesforce.loyalty.mobile.myntorewards.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatPromotionDate
import com.salesforce.loyalty.mobile.myntorewards.views.offers.PromotionEnrollPopup
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PromotionCard(page: Int, membershipPromo: List<Results>?) {
    var promoDescription = membershipPromo?.get(page)?.description ?: ""
    var promoName = membershipPromo?.get(page)?.promotionName ?: ""
    var endDate = membershipPromo?.get(page)?.endDate ?: ""
    var promotionEnrollmentRqr = membershipPromo?.get(page)?.promotionEnrollmentRqr ?: false
    var memberEligibilityCategory = membershipPromo?.get(page)?.memberEligibilityCategory ?: ""

    var currentPromotionDetailPopupState by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .background(Color.White)
            .clickable {
                currentPromotionDetailPopupState = true
            }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {

            Box(modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))) {
                Image(
                    painter = painterResource(R.drawable.promotion_card_placeholder),
                    contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                    modifier = Modifier
                        .size(289.dp, 154.dp)
                        .clip(RoundedCornerShape(10.dp)),
                )

                membershipPromo?.get(page)?.promotionImageUrl?.let {
                    GlideImage(
                        model = it,
                        contentDescription = stringResource(id = R.string.content_description_promotion_image),
                        modifier = Modifier
                            .size(289.dp, 154.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = promoName,
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
                    .width(289.dp),
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = promoDescription,
                fontWeight = FontWeight.Normal,
                fontFamily = font_sf_pro,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
                    .width(289.dp),
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (endDate.isNotEmpty()) {

                androidx.compose.material.Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle()
                        ) {
                            append(stringResource(id = R.string.prom_screen_expiration_text))
                        }
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append(formatPromotionDate(endDate))
                        }
                    },
                    fontFamily = font_sf_pro,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            //commenting as ios platform also removed this
            /*   Row(modifier = Modifier.height(45.dp))
               {
                   if(memberEligibilityCategory==MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED) {
                       JoinButtonProm {

                       }
                   }
               }*/


        }
    }

    if (currentPromotionDetailPopupState) {
        membershipPromo?.get(page)?.let {
            PromotionEnrollPopup(it)
            {
                currentPromotionDetailPopupState = false
            }
        }
    }
}

@Composable
fun PromotionEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = "Empty"
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.label_empty_promotions),
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.description_empty_promotions),
            fontWeight = FontWeight.Normal,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}


