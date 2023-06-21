package com.salesforce.loyalty.mobile.myntorewards.views.checkout

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.OrderScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ORDER_ID
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipProfileViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.CheckOutFlowViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MembershipProfileViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen

@Composable
fun OrderPlacedUI(
    navCheckOutFlowController: NavController,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    profileModel: MembershipProfileViewModelInterface
) {

    val orderDetails by checkOutFlowViewModel.orderDetailLiveData.observeAsState() // collecting livedata as state
    val orderID = navCheckOutFlowController.previousBackStackEntry?.savedStateHandle?.get<String>(
        ORDER_ID
    )

    //refreshing profile information and reward points. fresh rewards points will be pulled and cache will be
    //updated
    val context: Context = LocalContext.current
    LaunchedEffect(key1 = true) {
        profileModel.loadProfile(context, true)
    }
    orderID?.let {
        checkOutFlowViewModel.fetchOrderDetails(orderID)
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(OrderScreenBG)
    )
    {

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
        }
        Column(   modifier = Modifier
            .padding(start = 67.dp, end = 67.dp)
            .align(CenterHorizontally)
        ) {
            ContinueShoppingButton(navCheckOutFlowController)
            Spacer(modifier = Modifier.height(40.dp))
        }

    }


}

@Composable
fun ContinueShoppingButton(navCheckOutFlowController: NavController) {

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            navCheckOutFlowController.navigate(CheckOutFlowScreen.StartCheckoutFlowScreen.route) {
                popUpTo(0)
            }
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