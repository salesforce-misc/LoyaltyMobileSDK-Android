package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpoleLightBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

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