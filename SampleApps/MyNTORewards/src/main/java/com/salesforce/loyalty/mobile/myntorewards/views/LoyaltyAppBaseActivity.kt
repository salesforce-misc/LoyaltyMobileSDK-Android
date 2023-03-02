package com.salesforce.loyalty.mobile.myntorewards.views

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.view.WindowCompat
import com.google.accompanist.pager.*
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Tabs
import kotlinx.coroutines.launch

//Main Activity Application Entry Point
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            //HomeScreen2()
            MyPromotionFullScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //HomeScreen2()
    MyPromotionFullScreen()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen2() {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(MyProfileScreenBG)
    ) {


        Spacer(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(VibrantPurple40))



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(VibrantPurple40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.membership_card_logo),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .padding(start = 16.dp),
                contentScale = ContentScale.FillWidth
            )
            Image(
                painter = painterResource(id = R.drawable.research),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .padding(end = 16.dp),
                contentScale = ContentScale.FillWidth
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightPurple)
                .padding(top = 13.dp, bottom = 13.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Welcome, Julia Green! ",
                fontWeight = FontWeight.Normal,
                fontFamily = font_sf_pro,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = "17850 Points",
                fontWeight = FontWeight.Normal,
                fontFamily = font_sf_pro,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        Spacer(modifier = Modifier
            .height(16.dp)
            .fillMaxWidth())
        Column(  verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp, top = 14.dp)
                .wrapContentSize(Alignment.Center),
            ) {
            ProfileSubViewHeader("Promotions"){}
            Spacer(modifier = Modifier
                .height(16.dp)
                .fillMaxWidth())
            val pagerState = rememberPagerState()
            HorizontalPager(count = 4, state = pagerState) { page ->
                PromotionCard(page)
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = VibrantPurple40,
                inactiveColor = VibrantPurple90,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(CenterHorizontally)
            )
            Spacer(modifier = Modifier
                .height(16.dp)
                .fillMaxWidth())
        }
    }
}

@Composable
fun PromotionCard(page: Int)
{
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(Color.White)
    ) {
        Column( modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)){

        Image(
            painter = painterResource(id = R.drawable.image_2),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .padding(16.dp),
            contentScale = ContentScale.FillWidth
        )

        Text(
            text =""+ page+" Camping Fun For Entire Family",
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp),
            textAlign = TextAlign.Start,
            fontSize = 16.sp
        )
        Text(
            text = "The ultimate family camping destination is closer than you might think.",
            fontWeight = FontWeight.Normal,
            fontFamily = font_sf_pro,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp),
            textAlign = TextAlign.Start,
            fontSize = 12.sp
        )
        Text(
            text = "Expiration: 12/12/2022",
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp),
            textAlign = TextAlign.Start,
            fontSize = 12.sp
        )
            JoinButtonProm{

            }

        }
    }


}


@Composable
fun JoinButtonProm(openJoinPopup: () -> Unit) {

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            openJoinPopup()
        },
        colors = ButtonDefaults.buttonColors(LightPurple),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = stringResource(id = R.string.join_text),
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}

@Composable
fun MyPromotionFullScreen()
{

    Column( modifier = Modifier
        .fillMaxWidth()
        .background(LightPurple)
        .padding(bottom = 16.dp)
      ) {


        Spacer(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(Color.White))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 13.dp, bottom = 13.dp),

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
                painter = painterResource(id = R.drawable.search_icon_black),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .padding(16.dp),
                contentScale = ContentScale.FillWidth
            )
        }
       // Spacer(modifier = Modifier.height(50.dp).fillMaxWidth().background(Color.White))

        //CustomTabs()
        Row(modifier=Modifier.background(Color.White)){
            CustomTab2()
        }


        Row(modifier=Modifier.padding(start=16.dp, end= 16.dp, top = 16.dp)){
            PromotionItem()
        }
        Row(modifier=Modifier.padding(start=16.dp, end= 16.dp, top = 16.dp)){
            PromotionItem()
        }
        Row(modifier=Modifier.padding(start=16.dp, end= 16.dp, top = 16.dp)){
            PromotionItem()
        }
        Row(modifier=Modifier.padding(start=16.dp, end= 16.dp, top = 16.dp)){
            PromotionItem()
        }

    }

}

@Composable
fun PromotionItem()
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp)),

        horizontalArrangement = Arrangement.SpaceBetween,

    ) {
        Image(
            painter = painterResource(id = R.drawable.promotion_sample_image),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .width(130.dp)
                .height(166.dp),
            contentScale = ContentScale.FillWidth
        )
        Column(verticalArrangement =Arrangement.SpaceBetween,

            modifier = Modifier
                .fillMaxWidth()
                .height(166.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Text(
                    text = "Shop and get free product samples",
                    fontWeight = FontWeight.Medium,
                    fontFamily = font_sf_pro,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth(0.7f)
                )

                Image(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .width(24.dp)
                        .height(24.dp),
                    contentScale = ContentScale.FillWidth
                )


            }

            Text(
                text = "Get free sample on orders above \$ 5000 ",
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
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Free ",
                    fontWeight = FontWeight.Bold,
                    fontFamily = font_sf_pro,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    modifier = Modifier.padding()
                )

                Text(
                    text = "Exp 03/08/22  ",
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomTabs() {
    var selectedIndex by remember { mutableStateOf(0) }

    val list = listOf("All", "Active", "unEnrolled")
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    TabRow(selectedTabIndex = selectedIndex,
        modifier = Modifier,
       /* indicator = { tabPositions: List<TabPosition> ->
            Box {}
        }*/

        indicator = { tabPositions ->
            // on below line we are specifying the styling
            // for tab indicator by specifying height
            // and color for the tab indicator.
            TabRowDefaults.Indicator(
                //Modifier.pagerTabIndicatorOffset(tabPositions),
                height = 2.dp,
                color = VibrantPurple40
            )

        }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(10.dp)
                    .background(
                        Color.White
                    )
                else Modifier.background(Color.Blue),
                selected = selected,
                onClick = { selectedIndex = index

                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                          },
                text = { Text(text = text, color = Color(0xff6FAAEE)) }
            )
        }
    }
}

@Composable
fun CustomTab2()
{
    val tabItems = listOf(Tabs.Tab1, Tabs.Tab2, Tabs.Tab3)
    var selectedTab by remember {  mutableStateOf(0) }


    TabRow(selectedTabIndex = selectedTab,
        modifier = Modifier.fillMaxWidth().background(White),
        containerColor = White,
        divider = {},
        indicator = { tabPositions ->
            // on below line we are specifying the styling
            // for tab indicator by specifying height
            // and color for the tab indicator.
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab]).background(White),
                height = 2.dp,
                color = VibrantPurple40
            )


        }
    ) {

        tabItems.forEachIndexed { index, it ->

            Tab(selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(text = it.tabName) },
                selectedContentColor = VibrantPurple40,
                unselectedContentColor = TextGray,
                )
        }
    }
    when (selectedTab) {
    }
}
