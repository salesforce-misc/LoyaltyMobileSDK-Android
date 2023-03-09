package com.salesforce.loyalty.mobile.myntorewards.views.offers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro


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

    Column(modifier = Modifier.background(Color.Gray)) {

        Image(
            painter = painterResource(id = R.drawable.dummy_prom),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier.fillMaxWidth().height(400.dp),
            contentScale = ContentScale.FillWidth
        )


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

        Text(
            text = "Details",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Promotion issue a 10% discount on all vouchers",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )




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
                .align(Alignment.Start),
            textAlign = TextAlign.Start,
            fontSize = 12.sp
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(), onClick = {
            },
            colors = ButtonDefaults.buttonColors(LightPurple),
            shape = RoundedCornerShape(100.dp)

        ) {
            Text(
                text = "Shop",
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
        }


    }

}
