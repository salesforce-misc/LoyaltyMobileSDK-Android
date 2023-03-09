package com.salesforce.loyalty.mobile.myntorewards.views.offers

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.home.MyPromotionScreen


@Composable
fun PromotionEnrollPopup()
{

    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = { },
        properties = PopupProperties(focusable = true),
    ) {
        PromotionEnrollPopupUI()
    }

}
@Composable
fun PromotionEnrollPopupUI()
{

    Column(    modifier = Modifier
        .fillMaxHeight(0.9f)
        .background(Color.White, RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally)

    {

        val model: MyPromotionViewModel = viewModel()
        val membershipPromo by model.membershipPromotionLiveData.observeAsState() // collecting livedata as state
        val context: Context = LocalContext.current

        Image(
            painter = painterResource(id = R.drawable.dummy_prom),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Thanks Giving Promotion",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Details",
            fontWeight = FontWeight.Bold,
            color = TextDarkGray,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Promotion issue a 10% discount on all vouchers",
            fontWeight = FontWeight.Normal,
            color = TextGray,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )



        Spacer(modifier = Modifier.height(24.dp))

        androidx.compose.material.Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle()
                ) {
                    append(stringResource(id = R.string.prom_screen_expiration_text))
                }
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append("23/10/2022")
                }
            },
            fontFamily = font_sf_pro,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start).padding(start=16.dp),
            textAlign = TextAlign.Start,
            fontSize = 12.sp

        )


        Spacer(modifier = Modifier.height(80.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f), onClick = {
                model.enrollInPromotions(context, "PromoName")
            },
            colors = ButtonDefaults.buttonColors(VibrantPurple40),
            shape = RoundedCornerShape(100.dp)

        ) {
            Text(
                text = "Shop",
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color= White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
        }


    }

}
