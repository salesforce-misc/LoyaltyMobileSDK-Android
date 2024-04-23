package com.salesforce.loyalty.mobile.myntorewards.views.myprofile.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.BadgeBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BadgeView(badge: LoyaltyProgramBadgeListRecord, endDate: String?, blurBG: (Dp) -> Unit) {

    var badgePopupState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(106.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {
                badgePopupState = true
                blurBG(AppConstants.BLUR_BG)
            }
    )

    {

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            GlideImage(
                model = badge.imageUrl,
                contentDescription = badge.name,
                modifier = Modifier
                    .size(39.dp, 48.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            ) {
                it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.default_badge)
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BadgeBG, RoundedCornerShape(bottomStart=8.dp, bottomEnd= 8.dp))
        )

        {
            badge.name?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

    }
    if (badgePopupState) {
        BadgePopup(
            AppConstants.BADGES_ACHIEVED,
            badge,
            endDate,
            closePopup = {
                badgePopupState = false
                blurBG(AppConstants.NO_BLUR_BG)
            }
        )
    }
}