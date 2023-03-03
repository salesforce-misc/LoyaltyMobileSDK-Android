package com.salesforce.loyalty.mobile.myntorewards.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PromotionCard(page: Int, membershipPromo: List<Results>?) {
    var promoDescription = membershipPromo?.get(page)?.description ?: ""
    var promoName = membershipPromo?.get(page)?.promotionName ?: ""
    var endDate = membershipPromo?.get(page)?.endDate ?: ""

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .background(Color.White)
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

                membershipPromo?.get(page)?.imageUrl?.let {
                    GlideImage(
                        model = it,
                        contentDescription = "Image",
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
                    .align(Alignment.Start),
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
                    .align(Alignment.Start),
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (endDate.isNotEmpty()) {
                Text(
                    text = "Expiration: $endDate",
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            JoinButtonProm {

            }
        }
    }
}


