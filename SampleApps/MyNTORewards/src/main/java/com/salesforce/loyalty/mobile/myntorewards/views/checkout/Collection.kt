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
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.OrderTabs

@Composable
fun CheckOutFlowOrderSelectScreen(navCheckOutFlowController: NavController) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    )
    {

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
                        navCheckOutFlowController.popBackStack()
                    }
            )
        }
        OrderSelectHeader()
        OrderSelectTab()
        OrderSelectionDetail(navCheckOutFlowController)
    }
}

@Composable
fun OrderSelectTab() {

    //default tab selected as 0 which is OrderTabs.TabDetails
    var selectedTab by remember { mutableStateOf(0) }

    Row(modifier = Modifier.background(Color.White)) {

        val tabItems =
            listOf(OrderTabs.TabDetails, OrderTabs.TabReviews, OrderTabs.TabTnC)

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
                    unselectedContentColor = LighterBlack,
                )
            }
        }

    }

}


@Composable
fun OrderSelectHeader() {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(OrderScreenBG)
    )
    {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Outdoor Collection",
            fontWeight = FontWeight.Bold,
            color = Black1TextOrderScreen,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo_bold,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Double Points on Outdoor Product Category",
            fontWeight = FontWeight.Normal,
            color = Black2TextOrderScreen,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun OrderSelectionDetail(navCheckOutFlowController: NavController) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(VibrantIndigo95)
    )
    {

        RatingRow()
        OrderDescriptionRow()
        OrderLargeImageRow()
        SelectImageRow()
        OrderSelectSizeRow()
        OrderAvailableColoursRow()
        OrderQuantityRow()
        ButtonBuyOrAddCard(navCheckOutFlowController)

    }

}

@Composable
fun RatingRow() {
    Spacer(modifier = Modifier.height(18.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically,
        modifier = Modifier.fillMaxWidth(),

        ) {
        Text(
            text = "STOP",
            fontFamily = font_archivo,
            fontWeight = FontWeight.Bold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 16.dp)
        )

        Row(modifier = Modifier.padding(end = 17.dp)) {
            ReviewStarImage(true)
            ReviewStarImage(true)
            ReviewStarImage(true)
            ReviewStarImage(true)
            ReviewStarImage(true)
        }
    }
}

@Composable
fun OrderDescriptionRow() {
    Spacer(modifier = Modifier.height(18.dp))
    Text(
        text = "Women’s Flight Jacket",

        fontFamily = font_archivo,
        color = Black2TextOrderScreen,
        textAlign = TextAlign.Start,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .padding(start = 16.dp)
    )

}

@Composable
fun OrderLargeImageRow() {
    Spacer(modifier = Modifier.height(13.dp))
    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.jacket),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.FillWidth,
        )
    }


}

@Composable
fun SelectImageRow() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .horizontalScroll(
                rememberScrollState()
            )
    )
    {

        Image(
            painter = painterResource(id = R.drawable.jacket_1),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            modifier = Modifier.size(92.dp, 92.dp),
            contentScale = ContentScale.FillWidth,
        )
        Image(
            painter = painterResource(id = R.drawable.jacket_2),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            modifier = Modifier.size(92.dp, 92.dp),
            contentScale = ContentScale.FillWidth,
        )
        Image(
            painter = painterResource(id = R.drawable.jacket_3),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            modifier = Modifier.size(92.dp, 92.dp),
            contentScale = ContentScale.FillWidth,
        )
        Image(
            painter = painterResource(id = R.drawable.jacket_4),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            modifier = Modifier.size(92.dp, 92.dp),
            contentScale = ContentScale.FillWidth,
        )
    }
}


@Composable
fun OrderSelectSizeRow() {

    Spacer(modifier = Modifier.height(24.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    )
    {
        SelectionTextOrderScreen(R.string.text_select_size)

        Text(
            text = stringResource(id = R.string.text_view_size_chart),
            fontFamily = font_archivo,
            fontWeight = FontWeight.SemiBold,
            color = ColourViewSizeChart,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(end = 17.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically,
        modifier = Modifier.padding(start = 5.dp)

    ) {

        SelectionSizeTile("S", true)
        SelectionSizeTile("M", false)
        SelectionSizeTile("L", false)
        SelectionSizeTile("XL", false)
    }

}

@Composable
fun OrderAvailableColoursRow() {
    Spacer(
        modifier = Modifier
            .height(33.dp)
    )
    SelectionTextOrderScreen(R.string.text_available_colours)
    Spacer(
        modifier = Modifier
            .height(8.dp)
    )
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically,
        modifier = Modifier.padding(start = 5.dp, end = 16.dp)

    ) {
        SelectionImageTile(Color(0xFFEA001E))
        SelectionImageTile(Color(0xFF194E31))
        SelectionImageTile(Color(0xFFFE8F7D))
        SelectionImageTile(Color(0xFFC29EF1))
    }
}

@Composable
fun OrderQuantityRow() {

    Spacer(
        modifier = Modifier
            .height(32.dp)
    )

    SelectionTextOrderScreen(R.string.text_quantity)

    Spacer(
        modifier = Modifier
            .height(8.dp)
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()

    ) {

        Row {

            Text(
                text = "-",
                textAlign = TextAlign.Center,
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .size(43.dp, 36.dp)
                    .border(1.dp, Black3TextOrderScreen, RoundedCornerShape(10.dp))
                    .align(CenterVertically),
            )
            Spacer(
                modifier = Modifier
                    .width(12.dp)
            )

            Text(
                text = "1",
                textAlign = TextAlign.Center,
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(
                modifier = Modifier
                    .width(12.dp)
            )
            Text(
                text = "+",
                textAlign = TextAlign.Center,
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .size(43.dp, 36.dp)
                    .border(1.dp, Black3TextOrderScreen, RoundedCornerShape(10.dp))
                    .align(CenterVertically),
            )

        }
        Column()
        {
            Text(
                text = "$179",
                textAlign = TextAlign.Center,
                fontFamily = font_archivo_bold,
                color = LighterBlack,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = "Free shipping",
                textAlign = TextAlign.Center,
                fontFamily = font_archivo,
                color = Color.Black,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
            )

        }
    }

}

@Composable
fun ButtonBuyOrAddCard(navCheckOutFlowController: NavController) {

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            modifier = Modifier
                .fillMaxWidth(), onClick = {
                navCheckOutFlowController.navigate(CheckOutFlowScreen.OrderAddressAndPaymentScreen.route)
            },
            colors = ButtonDefaults.buttonColors(VibrantPurple40),
            shape = RoundedCornerShape(100.dp)

        ) {
            Text(
                text = stringResource(id = R.string.text_buy_now),
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
        }
        Spacer(
            modifier = Modifier
                .height(16.dp)

        )
        Button(
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, VibrantPurple40, RoundedCornerShape(100.dp)), onClick = {
            },
            shape = RoundedCornerShape(100.dp)


        ) {
            Text(
                text = stringResource(id = R.string.text_add_to_cart),
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = VibrantPurple40,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
        }
    }

}

@Composable
fun SelectionTextOrderScreen(text: Int) {

    Text(
        text = stringResource(text),
        fontFamily = font_archivo,
        fontWeight = FontWeight.SemiBold,
        color = LightBlack,
        textAlign = TextAlign.Start,
        fontSize = 13.sp,
        modifier = Modifier
            .padding(start = 17.dp)
    )

}

@Composable
fun SelectionImageTile(color: Color) {
    Spacer(
        modifier = Modifier
            .width(12.dp)
    )

    Text(
        text = "",
        textAlign = TextAlign.Center,
        fontFamily = font_sf_pro,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .size(43.dp, 36.dp)
            .background(color, RoundedCornerShape(10.dp)),

        )

}

@Composable
fun ReviewStarImage(starred: Boolean) {
    Image(
        painter = painterResource(id = R.drawable.star_1_3x),
        contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
        modifier = Modifier.size(12.dp, 11.dp),
        contentScale = ContentScale.FillWidth,
    )

}


@Composable
fun SelectionSizeTile(size: String, selected: Boolean) {

    var color = Black3TextOrderScreen
    var modifier = Modifier
        .width(43.dp)
        .border(1.dp, Black3TextOrderScreen, RoundedCornerShape(10.dp))
        .padding(top = 7.dp, bottom = 7.dp)

    if (selected) {
        color = Color.White
        modifier = Modifier
            .width(43.dp)
            .background(Black3TextOrderScreen, RoundedCornerShape(10.dp))
            .padding(top = 7.dp, bottom = 7.dp)
    }
    Spacer(
        modifier = Modifier
            .width(12.dp)
    )

    Text(
        text = size,
        textAlign = TextAlign.Center,
        fontFamily = font_archivo,
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}


