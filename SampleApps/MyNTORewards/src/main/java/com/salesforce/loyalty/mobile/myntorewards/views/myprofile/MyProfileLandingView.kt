package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState

@Composable
fun MyProfileLandingView(openProfileScreen: (profileScreenState: MyProfileScreenState) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {
        Spacer(modifier = Modifier.height(50.dp))
        ScreenTabHeader()

        Column(
            modifier = Modifier.verticalScroll(
                rememberScrollState()
            ),
        ) {
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
            UserInfoRow()
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
            ProfileCard()
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
                    .background(MyProfileScreenBG)
            )
            TransactionCard {
                openProfileScreen(it)
            }
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
                    .background(MyProfileScreenBG)
            )
            MyBenefitMiniScreenView {
                openProfileScreen(it)
            }
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
                    .background(MyProfileScreenBG)
            )

        }
    }
}