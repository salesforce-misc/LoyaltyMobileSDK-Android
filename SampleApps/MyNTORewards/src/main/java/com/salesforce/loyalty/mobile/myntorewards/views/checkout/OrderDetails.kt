package com.salesforce.loyalty.mobile.myntorewards.views.checkout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.HomeScreenState
import com.salesforce.loyalty.mobile.myntorewards.views.MainScreenStart
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.OrderTabs
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ShippingNavigationTabs

@Composable
fun OrderDetails() {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(OrderScreenBG)
            .verticalScroll(rememberScrollState())
    )
    {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(VibrantPurple40),
        ) {
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .background(VibrantPurple40)
            )
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
                    .clickable {
                        //  openHomeScreen(HomeScreenState.MAIN_VIEW)
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.membership_card_logo),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .padding(start = 16.dp),
                contentScale = ContentScale.FillWidth
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Order Details",
            fontFamily = font_archivo,
            fontWeight = FontWeight.Bold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 16.dp, top=8.dp, bottom = 8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        ShippingPaymentTab()
        Spacer(
            modifier = Modifier
                .height(24.dp)
        )
        ShippingAndPaymentRow()

        Spacer(
            modifier = Modifier
                .height(24.dp)
        )
        Column(modifier = Modifier.padding(start = 21.dp, end = 21.dp)) {


            Column(modifier = Modifier
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(start = 21.dp, end = 21.dp)) {
                AddressRow()
                EditDeleteAddressRow()
                ButtonDeliver()
            }
        }



    }
}

@Composable
fun ShippingAndPaymentRow()
{



    Row( horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()

    ) {

        Text(
            text = "Shipping Address",
            fontWeight = FontWeight.Bold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 14.sp,


        )
        Text(
            text = "Add New Address",
            fontWeight = FontWeight.Bold,
            color = VibrantPurple40,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 14.sp,
        )
    }
}



@Composable
fun AddressRow()
{
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp)
            .background(Color.White).padding(start = 11.dp, end = 11.dp),
    )
    {
        Text(
            text = "Nancy Tran,",
            fontWeight = FontWeight.SemiBold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
        Text(
            text = "4897 Davis Lane",
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
        Text(
            text = "Centennial\n" +
                    "Colorado",
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
        Text(
            text = "Zip code  80112",
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun EditDeleteAddressRow()
{
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )


    Row( horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 11.dp, end = 11.dp)
            .fillMaxWidth()

    ) {

        Text(
            text = "Edit  Address",
            fontWeight = FontWeight.SemiBold,
            color = VibrantPurple40,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 13.sp,


            )
        Text(
            text = "Delete  Address",
            fontWeight = FontWeight.SemiBold,
            color = VibrantPurple40,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 13.sp,


            )
    }
}

@Composable
fun ShippingPaymentTab()
{
    var selectedTab by remember { mutableStateOf(0) }

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
                        if(selectedTab == index)
                        {
                            androidx.compose.material3.Text(
                                text = stringResource(it.tabName),
                                fontFamily = font_archivo,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        else
                        {
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

}

@Composable
fun ButtonDeliver()
{

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            //  model.enrollInPromotions(context, "PromoName")
        },
        colors = ButtonDefaults.buttonColors(VibrantPurple40),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = "Deliver To This Address",
            fontFamily = font_archivo,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OrderDetails()
    //CheckOutFlowOrderSelectScreen()
}