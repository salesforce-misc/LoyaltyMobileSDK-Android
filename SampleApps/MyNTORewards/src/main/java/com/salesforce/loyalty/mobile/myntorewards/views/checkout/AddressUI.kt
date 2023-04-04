package com.salesforce.loyalty.mobile.myntorewards.views.checkout

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo
import com.salesforce.loyalty.mobile.myntorewards.utilities.HomeScreenState

@Composable
fun OrderAddressUI(switchPaymentTab: () -> Unit)
{

    ShippingAndPaymentRow()

    Spacer(
        modifier = Modifier
            .height(24.dp)
    )
    Column(modifier = Modifier.padding(start = 21.dp, end = 21.dp)) {


        Column(modifier = Modifier
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(start = 21.dp, end = 21.dp)) {
            AddressRow()
            EditDeleteAddressRow()
            ButtonDeliver{
                switchPaymentTab()
            }
        }
    }


}

@Composable
fun ShippingAndPaymentRow()
{



    Row( horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()

    ) {

        Text(
            text = "Shipping Address",
            fontWeight = FontWeight.Bold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 14.sp,


            )
        Text(
            text = "Add New Address",
            fontWeight = FontWeight.Bold,
            color = VibrantPurple40,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 14.sp,
        )
    }
}
@Composable
fun AddressRow()
{
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp)
            .background(Color.White).padding(start = 11.dp, end = 11.dp),
    )
    {
        Text(
            text = "Nancy Tran,",
            fontWeight = FontWeight.SemiBold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
        Text(
            text = "4897 Davis Lane",
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
        Text(
            text = "Centennial\n" +
                    "Colorado",
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
        Text(
            text = "Zip code  80112",
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun EditDeleteAddressRow()
{
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )


    Row( horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 11.dp, end = 11.dp)
            .fillMaxWidth()

    ) {

        Text(
            text = "Edit  Address",
            fontWeight = FontWeight.SemiBold,
            color = VibrantPurple40,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 13.sp,


            )
        Text(
            text = "Delete  Address",
            fontWeight = FontWeight.SemiBold,
            color = VibrantPurple40,
            textAlign = TextAlign.Start,
            fontFamily = font_archivo,
            fontSize = 13.sp,


            )
    }
}
@Composable
fun ButtonDeliver(switchPaymentTab: () -> Unit)
{

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            switchPaymentTab()
            //  model.enrollInPromotions(context, "PromoName")
        },
        colors = ButtonDefaults.buttonColors(VibrantPurple40),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = "Deliver To This Address",
            fontFamily = font_archivo,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

}

