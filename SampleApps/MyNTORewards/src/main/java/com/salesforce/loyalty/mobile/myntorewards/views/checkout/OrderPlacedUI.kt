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
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.OrderScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ORDER_ID
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.CheckOutFlowViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MembershipProfileViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.TextCustomButton
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.GameType
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.OrderPlacedGameSection
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.launch

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
            Spacer(modifier = Modifier.height(100.dp))
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
                text = stringResource(
                    id = R.string.text_order_placed, orderDetails?.orderNumber
                        ?: ""
                ),
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

        //TODO: Move these 2 fields to view model when API is ready
        val isGameUnlocked = true
        val gameType: GameType = listOf(GameType.SPIN_A_WHEEL, GameType.SCRATCH_CARD).random()
        OrderPlacedGameSection(isGameUnlocked, gameType)
        FooterButtonsView(navCheckOutFlowController, isGameUnlocked, gameType)
    }
}

@Composable
fun FooterButtonsView(
    navCheckOutFlowController: NavController,
    isGameUnlocked: Boolean,
    gameType: GameType
) {
    Column(   modifier = Modifier
        .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = CenterHorizontally,
    ) {
        val primaryButtonId = if (isGameUnlocked) {
            R.string.play_now_button_text
        } else {
            R.string.text_continue_shopping
        }

        val continueCheckOutFlow = {
            navCheckOutFlowController.navigate(CheckOutFlowScreen.StartCheckoutFlowScreen.route) {
                popUpTo(0)
            }
        }

        val navigateToGame = {
            val route = if (gameType == GameType.SCRATCH_CARD) {
                MoreScreens.ScratchCardScreen.route
            } else {
                MoreScreens.SpinWheelScreen.route
            }
            navCheckOutFlowController.navigate(route) {
                popUpTo(CheckOutFlowScreen.StartCheckoutFlowScreen.route)
            }
        }

        PrimaryButton(
            textContent = stringResource(primaryButtonId),
            onClick = {
                if (isGameUnlocked) {
                    navigateToGame()
                } else {
                    continueCheckOutFlow()
                }
            }
        )

        if (isGameUnlocked) {
            TextCustomButton(
                modifier = Modifier.padding(top = 16.dp),
                textContent = stringResource(id = R.string.text_continue_shopping),
                onClick = {
                    navCheckOutFlowController.navigate(CheckOutFlowScreen.StartCheckoutFlowScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}