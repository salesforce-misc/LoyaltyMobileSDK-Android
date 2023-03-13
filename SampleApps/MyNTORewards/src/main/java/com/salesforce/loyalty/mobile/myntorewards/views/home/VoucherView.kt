package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.MainScreenStart
import com.salesforce.loyalty.mobile.myntorewards.views.offers.ShopButton

@Composable
fun VoucherView(page: Int, membershipPromo: Any?) {
    Column(
        modifier = Modifier
            .width(165.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(bottom = 16.dp)
    )

    {
        Image(
            painter = painterResource(id = R.drawable.dummy_prom),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .width(165.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        {
            Text(
                text = "$50 off at Nike",
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro,
                color = LighterBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Online stores",
                fontWeight = FontWeight.Normal,
                color = LightBlack,
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append("Balance: ")
                    }
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append("$11")
                    }
                },
                fontWeight = FontWeight.Normal,
                color = LightBlack,
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()

            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle()
                    ) {
                        append("Valid till: ")
                    }
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append("05 Jan 2023")
                    }
                },
                fontWeight = FontWeight.Normal,
                color = LightBlack,
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()

            )


            Spacer(modifier = Modifier.height(12.dp))

            Box()
            {
                Image(
                    painter = painterResource(id = R.drawable.voucher_frame),
                    contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                    modifier = Modifier
                        .height(32.dp)
                        .height(145.dp),
                    contentScale = ContentScale.FillWidth
                )

                Row(modifier = Modifier
                    .height(32.dp)
                    .height(145.dp)) {

                    Text(
                        text = "84KFF88S",
                        fontWeight = FontWeight.Bold,
                        color = VoucherColourCode,
                        fontFamily = font_sf_pro,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                }

            }

        }
    }
}