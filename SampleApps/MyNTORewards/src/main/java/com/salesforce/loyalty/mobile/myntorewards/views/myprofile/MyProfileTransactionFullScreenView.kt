package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo_bold
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState

@Composable
fun MyProfileTransactionFullScreenView(openProfileScreen: (profileScreenState: MyProfileScreenState) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .clickable {
                    openProfileScreen(MyProfileScreenState.MAIN_VIEW)
                }
        )
        Text(
            text = stringResource(R.string.my_transactions),
            fontFamily = font_archivo_bold,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 11.5.dp, bottom = 11.5.dp)
        )
        TransactionFullScreenListView()
    }
}