package com.salesforce.loyalty.mobile.myntorewards.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MEMBER_ELIGIBILITY_CATEGORY_ELIGIBLE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatPromotionDate
import com.salesforce.loyalty.mobile.myntorewards.utilities.PromotionScreenState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.PromotionTabs
import com.salesforce.loyalty.mobile.myntorewards.views.offers.PromotionEnrollPopup
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results


@Composable
fun MyPromotionScreen(membershipPromo: List<Results>?, openHomeScreen: (promotionScreenState: PromotionScreenState) -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightPurple)
            .padding(bottom = 16.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )

        MyPromotionScreenHeader()
        var selectedTab by remember { mutableStateOf(0) }

        Row(modifier = Modifier.background(Color.White)) {

            val tabItems =
                listOf(PromotionTabs.TabAll, PromotionTabs.TabActive, PromotionTabs.TabUnEnrolled)

            TabRow(selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                containerColor = Color.White,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .background(Color.White),
                        height = 2.dp,
                        color = VibrantPurple40
                    )
                })
            {
                tabItems.forEachIndexed { index, it ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(text = stringResource(it.tabName)) },
                        selectedContentColor = VibrantPurple40,
                        unselectedContentColor = TextGray,
                    )
                }
            }

        }

        membershipPromo?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                items(it) {


                    when (selectedTab) {
                        0 -> {
                            PromotionItem(it)
                            {
                                openHomeScreen(it)
                            }
                        }
                        1 -> {
                            if (it.memberEligibilityCategory == MEMBER_ELIGIBILITY_CATEGORY_ELIGIBLE) {
                                PromotionItem(it)
                                {
                                    openHomeScreen(it)
                                }
                            }
                        }
                        2 -> {
                            if (it.memberEligibilityCategory == MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED) {
                                PromotionItem(it)
                                {
                                    openHomeScreen(it)
                                }
                            }
                        }
                    }

                }
            }
        }

    }

}

@Composable
fun MyPromotionScreenHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 13.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = "My Promotions",
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 16.dp)
        )

        Image(
            painter = painterResource(R.drawable.search_icon_black),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PromotionItem(results: Results, openHomeScreen: (promotionScreenState: PromotionScreenState) -> Unit) {

    val description = results.description ?: ""
    var endDate = results.endDate ?: ""

    var currentPromotionDetailPopupState by remember { mutableStateOf(false) }

    if (currentPromotionDetailPopupState) {

        PromotionEnrollPopup(results,
            closePopup = {
                currentPromotionDetailPopupState = false
            },
            openHomeScreen = { openHomeScreen(it)
            }
        )
    }




    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                currentPromotionDetailPopupState = true
            }
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Box() {
            Image(
                painter = painterResource(R.drawable.promotionlist_image_placeholder),
                contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .size(130.dp, 166.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillWidth
            )
            GlideImage(
                model = results.promotionImageUrl,
                contentDescription = description,
                modifier = Modifier
                    .size(130.dp, 166.dp)
                    .clip(RoundedCornerShape(10.dp)),

                contentScale = ContentScale.Crop
            )
        }



        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .height(166.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                results.promotionName?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font_sf_pro,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(0.7f)
                    )
                }

                Image(
                    painter = painterResource(id = com.salesforce.loyalty.mobile.MyNTORewards.R.drawable.heart),
                    contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .width(24.dp)
                        .height(24.dp),
                    contentScale = ContentScale.FillWidth
                )

            }

            Text(
                text = description,
                fontWeight = FontWeight.Normal,
                fontFamily = font_sf_pro,
                color = TextDarkGray,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                maxLines = 2,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(end = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 24.dp, end = 21.dp),
                horizontalArrangement = Arrangement.End,
            ) {

                //Not part of MVP
                /*Text(
                    text = "Free ",
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    modifier = Modifier.padding()
                )*/

                if (endDate.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.prom_full_screen_expiration_text) + " " + formatPromotionDate(
                            endDate
                        ),
                        fontWeight = FontWeight.Bold,
                        fontFamily = font_sf_pro,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .background(Color.Black, shape = RoundedCornerShape(4.dp))
                            .padding(start = 12.dp, end = 12.dp)
                    )
                }

            }

        }
    }

}
