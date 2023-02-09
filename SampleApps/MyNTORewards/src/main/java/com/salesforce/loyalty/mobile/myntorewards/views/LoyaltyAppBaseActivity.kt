package com.salesforce.loyalty.mobile.myntorewards.views

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*

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
         MainScreenStart()
            //MyProfileScreen()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
 MainScreenStart()
    //MyProfileScreen()
}

@Composable
fun MyProfileScreen() {

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {

        Column(modifier = Modifier
            .fillMaxHeight(0.13f)
            .fillMaxWidth()
        ) {
            ScreenTabHeader()
        }
        Column(modifier = Modifier
            .fillMaxHeight(0.13f)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)) {
            UserInfoRow()
        }
        Column(modifier = Modifier
            .fillMaxHeight(0.4f)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .background(TextPurpoleLightBG)) {
            ProfileCard()
        }
        //Transaction
        Column( modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(TextPurpoleLightBG)
            .wrapContentSize(Alignment.Center)
        ) {
            TransactionCard()
        }
    }
}

@Composable
fun ScreenTabHeader()
{
    Text(
        text = "My Profile",
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        modifier = Modifier.padding(start = 16.dp, top = 50.dp)
    )

}
@Composable
fun UserInfoRow()
{
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = com.salesforce.loyalty.mobile.MyNTORewards.R.drawable.user_pic),
            contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(start = 16.dp),
            contentScale = ContentScale.FillWidth
        )
        Column( modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(start = 5.dp)
        ) {

            Text(
                text = "Julia Green",
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Text(
                text = "24252627",
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
        Column( modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Image(
                painter = painterResource(id = com.salesforce.loyalty.mobile.MyNTORewards.R.drawable.edit_icon),
                contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                modifier= Modifier.width(42.dp),
                contentScale = ContentScale.FillWidth
            )
        }

    }

}

@Composable
fun ProfileCard()
{
    Card(shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .background(TextPurpoleLightBG)
    )
    {

        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(4.dp))
        )
        {
            Image(
                painter = painterResource(id = com.salesforce.loyalty.mobile.MyNTORewards.R.drawable.user_membership_card_bg),
                contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 16.dp)
                    .weight(0.25f),
                    verticalArrangement = Arrangement.Top,) {

                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 32.dp)
                        ) {
                        Text(
                            text = "Gold",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .background(
                                    MembershipTierBG, RoundedCornerShape(30.dp)
                                )
                                .padding(top = 3.dp, start = 16.dp, end = 16.dp, bottom = 3.dp)
                        )
                        Image(
                            painter = painterResource(id = com.salesforce.loyalty.mobile.MyNTORewards.R.drawable.membership_card_logo),
                            contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                            modifier = Modifier.width(96.dp),
                            contentScale = ContentScale.FillWidth

                        )
                    }



                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .padding(start = 32.dp),
                    verticalArrangement = Arrangement.Top,) {
                    Text(
                        text = "17850",
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp
                    )

                    Text(
                        text = "100 points expiring on Oct 20 2022",
                        fontFamily = font_sf_pro,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )


                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 30.dp)
                    .weight(0.3f),
                    horizontalAlignment = Alignment.End


                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Rewards Points",
                            fontFamily = font_sf_pro,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                        Image(
                            painter = painterResource(id = com.salesforce.loyalty.mobile.MyNTORewards.R.drawable.sample_qr_code),
                            contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                            modifier = Modifier
                                .width(46.dp)
                                .height(46.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                }

            }

            }

        }
       /* */
        // Text(text = "Round corner shape", modifier = paddingModifier)
    }

@Composable
fun TransactionCard()
{
    Column() {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth().padding(end = 16.dp, start = 16.dp, top = 16.dp)
        ) {
            Text(
                text = "My Transactions",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
            Text(
                text = "View All",
                fontWeight = FontWeight.Bold,
                color = VibrantPurple40,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
            )
        }
    }
    Column(modifier = Modifier
        .fillMaxWidth().fillMaxHeight().padding(16.dp)) {
        ListItemTransaction()
        ListItemTransaction()
        ListItemTransaction()
    }


}


@Composable
fun ListItemTransaction()
{
    Spacer(modifier = Modifier.height(12.dp))

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth().background(Color.White,shape = RoundedCornerShape(8.dp)).padding(16.dp)
    ) {

        Column(  modifier = Modifier.weight(0.15f)) {
            Image(
                painter = painterResource(id = com.salesforce.loyalty.mobile.MyNTORewards.R.drawable.transaction_icon_1),
                contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        Column(  modifier = Modifier.weight(0.6f)) {
            Text(
                text = "Promotion Enrollment",
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
            )
            Text(
                text = "05’ June 2022",
                fontFamily = font_sf_pro,
                color = TextDarkGray,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
            )

        }
        Column(  modifier = Modifier.weight(0.25f)) {
            Text(
                text = "+1500 Pts",
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Bold,
                color = TextGreen,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier
            )
        }
    }

}