package com.salesforce.loyalty.mobile.myntorewards.views.myprofile.badges

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.POPUP_ROUNDED_CORNER_SIZE
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BADGE_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BADGE_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BADGE_POPUP
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton

@Composable
fun BadgePopup(
    badgeType:String,
    badges: LoyaltyProgramBadgeListRecord,
    endDate: String?,
    closePopup: () -> Unit,
) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 1600),
        onDismissRequest = { closePopup() },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
    ) {
        BadgePopupUI(
            badgeType,
            badges,
            endDate,
            closePopup = {
                closePopup()
            },
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BadgePopupUI(
    badgeType:String="",
    badge: LoyaltyProgramBadgeListRecord,
    endDate: String?,
    closePopup: () -> Unit,
) {

    var expiringText by remember { mutableStateOf("") }
    var popupBGColour by remember { mutableStateOf(VibrantPurple90) }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.55f)
            .background(Color.White, RoundedCornerShape(POPUP_ROUNDED_CORNER_SIZE))
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )

    {

        Column(modifier = Modifier.testTag(TEST_TAG_BADGE_POPUP)) {
            Box()
            {
                Box(
                    modifier = Modifier
                        .background(
                            popupBGColour,
                            RoundedCornerShape(
                                POPUP_ROUNDED_CORNER_SIZE,
                                POPUP_ROUNDED_CORNER_SIZE,
                                0.dp,
                                0.dp
                            )
                        )
                        .fillMaxWidth()
                        .height(181.dp)
                )
                {
                    if(badgeType== AppConstants.BADGES_AVAILABLE) {
                        LoadIconImagePopup(R.drawable.loyalty_available_badge, 107.dp, 128.dp)
                    }
                    else if(badgeType== AppConstants.BADGES_EXPIRED) {
                        LoadIconImageExpiredPopup(badge.imageUrl,107.dp, 128.dp )
                    }
                    else{
                        LoadIconImagePopup(badge.imageUrl, 107.dp, 128.dp)
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.close_button_without_bg),
                        contentDescription = stringResource(R.string.cd_close_button_badge_popup),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .padding(3.dp)
                            .clickable {
                                closePopup()
                            }

                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            badge.name?.let {
                Text(
                    text = badge.name,
                    fontWeight = FontWeight.SemiBold,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 24.sp,
                    fontFamily = font_sf_pro,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 16.dp).testTag(TEST_TAG_BADGE_NAME)
                        .fillMaxWidth()
                )
            }


            if (!endDate.isNullOrEmpty()) {

                if(Common.isEndDateExpired(endDate)){
                    expiringText= stringResource(id = R.string.text_expired_on)
                    popupBGColour=  VibrantPurple90.copy(alpha = 0.8f)
                }
                else{
                    expiringText= stringResource(id = R.string.text_badge_expires_on)
                    popupBGColour= VibrantPurple90
                }

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle()
                        ) {
                            append(expiringText)
                        }
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.SemiBold)
                        ) {
                            append(endDate)
                        }
                    },
                    fontFamily = font_sf_pro,
                    color = LightBlack,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp

                )
            } else {

                Text(
                    text = stringResource(id = R.string.badge_text_not_achieved_yet),
                    fontFamily = font_sf_pro,
                    color = LightBlack,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp

                )
            }



            Text(
                text = stringResource(id = R.string.badge_learn_more),
                fontWeight = FontWeight.SemiBold,
                color = LightBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                fontFamily = font_sf_pro,
                modifier = Modifier
                    .padding(start = 20.dp, end = 16.dp)
                    .fillMaxWidth().testTag(TEST_TAG_BADGE_DESCRIPTION)
            )

            Text(
                text = badge.description,
                fontWeight = FontWeight.Normal,
                color = LightBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                fontFamily = font_sf_pro,
                modifier = Modifier
                    .padding(start = 20.dp, end = 16.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

        }
        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(textContent=stringResource(id = R.string.close_text),  modifier = Modifier
            .width(300.dp), onClick = {
          closePopup()
        })
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadIconImagePopup(iconImage: Any?, width: Dp, height: Dp){
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background( VibrantPurple90.copy(alpha = 0.8f))
    )
    {
        GlideImage(
            model = iconImage,
            contentDescription = stringResource(id = R.string.cd_badge_popup_icon),
            modifier = Modifier
                .size(width, height)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            contentScale = ContentScale.Crop
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.default_badge)

        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadIconImageExpiredPopup(iconImage: Any?, width: Dp, height: Dp){

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background( VibrantPurple90.copy(alpha = 0.8f))
    )
    {

        GlideImage(
            model = iconImage,
            contentDescription = stringResource(R.string.cd_badge_icon_full_screen),
            modifier = Modifier
                .size(width, height)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)).background(
                    color = VibrantPurple90.copy(alpha = 0.8f)
                )
                .align(Alignment.Center)
            ,
            contentScale = ContentScale.Fit
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.default_badge)
        }
        Box(modifier = Modifier
            .background(
                color = VibrantPurple90.copy(alpha = 0.8f)
            ).size(width, height)){

            GlideImage(
                model = R.drawable.expired_badge_icon,
                contentDescription = stringResource(R.string.cd_badge_icon_full_screen),
                modifier = Modifier
                    .size(48.dp, 48.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )

        }

    }

}