package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Spacer(modifier = Modifier.height(50.dp))
        ScreenTabHeader()
        Spacer(modifier = Modifier.height(24.dp))
        UserInfoRow()
        Spacer(modifier = Modifier.height(24.dp))
        ProfileCard()
        Spacer(modifier = Modifier.height(24.dp))
        TransactionCard()
    }
}

