package com.salesforce.loyalty.mobile.myntorewards.views.checkout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.PromotionScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.CheckOutFlowViewModel

@Composable
fun OrderPlacedUI(
    orderID: String,
    openHomeScreen: (promotionScreenState: PromotionScreenState) -> Unit
) {

    val model: CheckOutFlowViewModel = viewModel()  //fetching reference of viewmodel
    val orderDetails by model.orderDetailLiveData.observeAsState() // collecting livedata as state
    model.fetchOrderDetails(orderID)

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(OrderScreenBG)
            .verticalScroll(rememberScrollState())
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(VibrantPurple40),
        ) {

            Spacer(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(VibrantPurple40)
            )

            Image(
                painter = painterResource(id = R.drawable.membership_card_logo),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .padding(start = 16.dp),
                contentScale = ContentScale.FillWidth
            )

        }

        Column(
            modifier = Modifier
                .padding(start = 67.dp, end = 67.dp)
                .align(CenterHorizontally)
        ) {
            Spacer(modifier = Modifier.height(128.dp))
            Image(
                painter = painterResource(id = R.drawable.circle_check_box),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .width(137.dp)
                    .height(137.dp)
                    .align(CenterHorizontally),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = stringResource(id = R.string.text_order_placed) + " " + (orderDetails?.orderNumber
                    ?: ""),
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                modifier = Modifier.align(CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = stringResource(id = R.string.text_payment_complete_msg),
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(100.dp))
            ContinueShoppingButton {
                openHomeScreen(it)
            }
        }

    }


}

@Composable
fun ContinueShoppingButton(openHomeScreen: (promotionScreenState: PromotionScreenState) -> Unit) {

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            openHomeScreen(PromotionScreenState.MAIN_VIEW)
        },
        colors = ButtonDefaults.buttonColors(VibrantPurple40),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = stringResource(id = R.string.text_continue_shopping),
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

}