package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG

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
        Column(
            modifier = Modifier
                .fillMaxHeight(0.13f)
                .fillMaxWidth()
        ) {
            ScreenTabHeader()
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.13f)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ) {
            UserInfoRow()
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .background(TextPurpoleLightBG)
        ) {
            ProfileCard()
        }
        //Transaction
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(TextPurpoleLightBG)
                .wrapContentSize(Alignment.Center)
        ) {
            TransactionCard()
        }
    }
}