package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.PromotionTabs
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results


@Composable
fun MyPromotionScreen() {
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
        val model: MyPromotionViewModel = viewModel()
        val membershipPromo by model.membershipPromotionLiveData.observeAsState() // collecting livedata as state
        val context: Context = LocalContext.current
        model.promotionAPI(context)
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
                        }
                        1 -> {
                            if (it.promotionEnrollmentRqr == false) {
                                PromotionItem(it)
                            }
                        }
                        2 -> {
                            if (it.promotionEnrollmentRqr == true) {
                                PromotionItem(it)
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


@Composable
fun PromotionScreenTabs() {

}

@Composable
fun MyPromotionList(currentTabState: MutableState<PromotionTabs.TabAll>) {

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PromotionItem(results: Results) {

    val description = results.description ?: ""
    var endDate = results.endDate ?: ""

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
                model = results.imageUrl,
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
                modifier = Modifier.padding(start = 16.dp)
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
                        text = "Exp $endDate  ",
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
