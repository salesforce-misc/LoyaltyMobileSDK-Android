package com.salesforce.loyalty.mobile.myntorewards.views

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.ViewPagerSupport.ViewPagerSupport.imageID
import com.salesforce.loyalty.mobile.myntorewards.utilities.ViewPagerSupport.ViewPagerSupport.screenText


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
            OnboardingScreenBox()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreenBox() {
    Box(modifier = Modifier.fillMaxSize())
    {

        //setting up swipe pager and images accordingly
        val pagerState = rememberPagerState()
        HorizontalPager(count = 3, state = pagerState) { page ->
            Image(
                painter = painterResource(id = imageID(page)),
                contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.screen_bottom_black_fading),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .align(alignment = Alignment.BottomEnd),
            verticalArrangement = Arrangement.Bottom,

            )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(start = 24.dp),
                verticalArrangement = Arrangement.Bottom,
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.application_logo),
                    contentDescription = stringResource(id = R.string.cd_app_logo)
                )

                //Swipe indicator
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    activeColor = Color.White,
                    inactiveColor = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                )


                //Updating the screen text dynamically as page swipe
                val stringText = remember { mutableStateOf("") }
                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        stringText.value = screenText(page)
                    }
                }
                Text(
                    text = stringText.value,
                    fontFamily = font_sf_pro,
                    color = Color.White,
                    fontSize = 34.sp
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
            )
            {
                JoinLoginButtonBox()
            }

            //Only for spacing
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                verticalArrangement = Arrangement.Bottom,
            )
            {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OnboardingScreenBox()
}