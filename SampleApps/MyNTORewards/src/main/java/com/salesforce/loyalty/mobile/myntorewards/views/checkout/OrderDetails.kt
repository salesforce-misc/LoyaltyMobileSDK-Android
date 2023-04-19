package com.salesforce.loyalty.mobile.myntorewards.views.checkout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ShippingNavigationTabs

@Composable
fun OrderDetails(navCheckOutFlowController: NavController) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(OrderScreenBG)
            .verticalScroll(rememberScrollState())
    )
    {
        //default tab selected as 0 which is ShippingNavigationTabs.TabShipping
        var selectedTab by remember { mutableStateOf(0) }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .clickable {
                        if (selectedTab == 0) {
                            navCheckOutFlowController.popBackStack()
                        } else {
                            selectedTab = 0
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.text_order_details),
            fontFamily = font_archivo,
            fontWeight = FontWeight.Bold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))



        Row(modifier = Modifier.background(Color.White)) {

            val tabItems =
                listOf(ShippingNavigationTabs.TabShipping, ShippingNavigationTabs.TabPayment)

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

                        text = {
                            if (selectedTab == index) {
                                androidx.compose.material3.Text(
                                    text = stringResource(it.tabName),
                                    fontFamily = font_archivo,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            } else {
                                androidx.compose.material3.Text(
                                    text = stringResource(it.tabName),
                                    fontFamily = font_archivo,
                                )
                            }
                        },
                        selectedContentColor = LighterBlack,
                        unselectedContentColor = VeryLightGray,
                    )
                }
            }

        }
        Spacer(
            modifier = Modifier
                .height(24.dp)
        )

        when (selectedTab) {

            0 -> OrderAddressUI {
                selectedTab = 1
            }
            1 -> PaymentsUI(navCheckOutFlowController)
        }


    }
}

//This is needed while development the UI as we need to constantly see how our UI will look like.
/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //OrderDetails()
    //CheckOutFlowOrderSelectScreen()
}*/
