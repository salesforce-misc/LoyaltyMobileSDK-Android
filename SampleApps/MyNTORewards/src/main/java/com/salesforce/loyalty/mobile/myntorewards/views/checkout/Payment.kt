package com.salesforce.loyalty.mobile.myntorewards.views.checkout

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CONFIRM_ORDER_BUTTON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PAYMENT_UI_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OrderPlacedState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.CheckOutFlowViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MembershipProfileViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.VoucherViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.GameType
import com.salesforce.loyalty.mobile.myntorewards.views.myCheckBoxColors
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen


@Composable
fun PaymentsUI(
    navCheckOutFlowController: NavController,
    voucherModel: VoucherViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    profileModel: MembershipProfileViewModelInterface,
    gameViewModel: GameViewModel
) {
    val membershipProfile by profileModel.membershipProfileLiveData.observeAsState()
    val context: Context = LocalContext.current
    LaunchedEffect(key1 = true) {
        profileModel.loadProfile(context)
    }

    Box() {
        var isInProgress by remember { mutableStateOf(false) }
        var isGameQueryInProgress by remember { mutableStateOf(false) }
        Column(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .verticalScroll(
                rememberScrollState()
            )
            .testTag(TEST_TAG_PAYMENT_UI_CONTAINER)
        ) {

            Spacer(modifier = Modifier.height(23.dp))
            VoucherRow(voucherModel)
            Spacer(modifier = Modifier.height(24.dp))
            membershipProfile?.memberCurrencies?.get(0)?.pointsBalance.let {
                PointsRow(it.toString())
            }
            AmountPaybleRow()
            Spacer(modifier = Modifier.height(24.dp))

            CardNumberRow()
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(16.dp))

            val gamesViewState by gameViewModel.gamesViewState.observeAsState()
            val games by gameViewModel.gamesLiveData.observeAsState()
            val orderCreationResponse by checkOutFlowViewModel.orderCreationResponseLiveData.observeAsState()

            val orderPlaceStatus by checkOutFlowViewModel.orderPlacedStatusLiveData.observeAsState(OrderPlacedState.ORDER_PLACED_DEFAULT_EMPTY) // collecting livedata as state

            if (orderPlaceStatus == OrderPlacedState.ORDER_PLACED_SUCCESS) {
                LaunchedEffect(key1 = true){
                    val gameParticipantRewardId =
                        orderCreationResponse?.gameParticipantRewardId
                    isGameQueryInProgress = true
                    gameViewModel.getGames(context,
                        gameParticipantRewardId = gameParticipantRewardId,
                        false
                    )
                }
                /*Toast.makeText(
                    LocalContext.current,
                    "Order Success:: " + checkOutFlowViewModel.orderIDLiveData.value.toString(),
                    Toast.LENGTH_LONG
                ).show()*/

            } else if (orderPlaceStatus == OrderPlacedState.ORDER_PLACED_FAILURE) {
                Toast.makeText(LocalContext.current, "orderPlaceStatus Failed", Toast.LENGTH_LONG)
                    .show()
                checkOutFlowViewModel.resetOrderPlacedStatusDefault()
                isInProgress = false
            }

            when (gamesViewState) {
                GamesViewState.GamesFetchSuccess -> {
                    if (isGameQueryInProgress) {
                        var gameType: String? = null
                        games?.gameDefinitions?.first()?.let { gameDef ->
                            gameType = GameType.from(gameDef.type)?.gameType
                        }
                        val orderID =
                            orderCreationResponse?.orderId
                        val gameParticipantRewardId =
                            orderCreationResponse?.gameParticipantRewardId
                        checkOutFlowViewModel.resetOrderPlacedStatusDefault()
                        navCheckOutFlowController.currentBackStackEntry?.savedStateHandle?.apply {
                            set(AppConstants.KEY_ORDER_ID, orderID)
                            if (gameType != null) {
                                set(
                                    AppConstants.KEY_GAME_PARTICIPANT_REWARD_ID,
                                    gameParticipantRewardId
                                )
                                set(AppConstants.KEY_GAME_TYPE, gameType)
                            }
                        }
                        navCheckOutFlowController.navigate(CheckOutFlowScreen.OrderConfirmationScreen.route)
                        isInProgress = false
                        isGameQueryInProgress = false
                    }
                }
                GamesViewState.GamesFetchFailure -> {
                    if (isGameQueryInProgress) {
                        val orderID =
                            checkOutFlowViewModel.orderCreationResponseLiveData.value?.orderId
                        checkOutFlowViewModel.resetOrderPlacedStatusDefault()

                        navCheckOutFlowController.currentBackStackEntry?.savedStateHandle?.apply {
                            set(AppConstants.KEY_ORDER_ID, orderID)
                        }
                        navCheckOutFlowController.navigate(CheckOutFlowScreen.OrderConfirmationScreen.route)
                        isInProgress = false
                        isGameQueryInProgress = false
                    }
                }
                else -> {}
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(), onClick = {
                    isInProgress = true
                    checkOutFlowViewModel.placeOrderAndGetParticipantReward()
                },
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.text_confirm_order),
                    fontFamily = font_archivo,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .testTag(TEST_TAG_CONFIRM_ORDER_BUTTON)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (isInProgress) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.Center)
            )
        }

    }

}

@Composable
fun VoucherRow(voucherModel: VoucherViewModelInterface) {
    Column(modifier = Modifier.background(Color.White, RoundedCornerShape(20.dp))) {

        Text(
            text = stringResource(id = R.string.vouchers),
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 11.dp, top = 17.dp, end = 45.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        VoucherMenuBox(voucherModel)
        Spacer(modifier = Modifier.height(16.dp))


    }

}

@Composable
fun AmountPaybleRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(start = 11.dp, end = 11.dp, bottom = 16.dp, top = 22.dp)
    ) {

        Text(
            text = stringResource(id = R.string.text_amount_payble),
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
        )
        Text(
            text = "$207",
            fontFamily = font_archivo,
            fontWeight = FontWeight.ExtraBold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            modifier = Modifier
        )

    }
}

@Composable
fun PointsRow(points: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 11.dp, bottom = 16.dp, top = 22.dp)
    ) {


            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val checkedUseRewardPoint = remember { mutableStateOf(false) }
                Checkbox(
                    checked = checkedUseRewardPoint.value,
                    onCheckedChange = {
                        checkedUseRewardPoint.value = it
                    },
                    colors = myCheckBoxColors()
                )
                Text(
                    text = stringResource(id = R.string.text_use_my_points),
                    fontFamily = font_archivo,
                    fontWeight = FontWeight.SemiBold,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier
                )
        }

        Text(
            text = stringResource(id = R.string.text_points_available) + points,
            fontFamily = font_archivo,
            fontWeight = FontWeight.SemiBold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
        )

    }
}


@Composable
fun CardNumberRow() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(start = 11.dp, end = 11.dp, bottom = 16.dp, top = 22.dp)
    )
    {

        Text(
            text = stringResource(id = R.string.text_card_number),
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 16.dp)
        ) {

            CardInputField("0092")
            CardInputField("0230")
            CardInputField("0935")
            CardInputField("2800")

        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 16.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(0.7f)
            ) {

                Text(
                    text = stringResource(id = R.string.text_expiry_date),
                    fontFamily = font_archivo,
                    fontWeight = FontWeight.Medium,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    MonthDropDownBox()
                    YearDropDownBox()

                }
            }

            Column(
                modifier = Modifier
                    .weight(0.3f)
            ) {

                Text(
                    text = stringResource(id = R.string.text_security_code),
                    fontFamily = font_archivo,
                    fontWeight = FontWeight.Medium,
                    color = LighterBlack,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(10.dp))
                CVVInputField("298")

            }


        }


    }
}

@Composable
fun CardInputField(text: String) {
    TextField(
        value = text,
        onValueChange = {

        },
        modifier = Modifier
            .width(70.dp)
            .width(44.dp),

        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

        textStyle = TextStyle(
            fontFamily = font_archivo,
            color = CardFieldText,
            fontSize = 12.sp,
        ),

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = DropDownMenuBG,
            focusedIndicatorColor = Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.Transparent
        ),

        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
fun CVVInputField(text: String) {
    TextField(
        value = text,
        onValueChange = {

        },
        modifier = Modifier
            .width(111.dp)
            .width(44.dp),

        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

        textStyle = TextStyle(
            fontFamily = font_archivo,
            color = CardFieldText,
            fontSize = 12.sp,
        ),

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = DropDownMenuBG,
            focusedIndicatorColor = Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.Transparent
        ),

        shape = RoundedCornerShape(16.dp),
    )
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoucherMenuBox(voucherModel: VoucherViewModelInterface) {
    voucherModel.loadVoucher(LocalContext.current)
    val options = mutableListOf<String>()
    val vouchers by voucherModel.voucherLiveData.observeAsState()
    vouchers?.let { it ->
        for (voucher in it) {
            voucher.voucherDefinition?.let { name ->
                options.add(name)
            }
        }
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember {
        if (options.isNotEmpty()) {
            mutableStateOf(options[0])
        } else {
            mutableStateOf("")
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .padding(start = 11.dp, end = 11.dp)
            .background(DropDownMenuBG, RoundedCornerShape(15.dp))
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = selectedOptionText,
                fontFamily = font_archivo,
                fontWeight = FontWeight.Medium,
                color = CardFieldText,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 47.dp)
            )

            if (expanded) {
                Image(
                    painter = painterResource(id = R.drawable.uparrow),
                    contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 13.5.dp, bottom = 13.5.dp, end = 15.dp)
                )

                //expand image needs to be here yet to be provided by UX designer
            } else {
                Image(
                    painter = painterResource(id = R.drawable.down_arrow),
                    contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 13.5.dp, bottom = 13.5.dp, end = 15.dp)
                )
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MonthDropDownBox() {
    val options = listOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .background(DropDownMenuBG, RoundedCornerShape(15.dp))
    ) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(83.dp)
        ) {

            Text(
                text = selectedOptionText,
                fontFamily = font_archivo,
                fontWeight = FontWeight.Medium,
                color = CardFieldText,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 15.dp)
            )

            if (expanded) {
                Image(
                    painter = painterResource(id = R.drawable.uparrow),
                    contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 13.5.dp, bottom = 13.5.dp, end = 15.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.down_arrow),
                    contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp, end = 15.dp)
                )
            }
        }


        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun YearDropDownBox() {
    val options = listOf("24", "25", "26", "27", "28", "29", "30", "31", "32", "33")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .padding(start = 11.dp, end = 11.dp)
            .background(DropDownMenuBG, RoundedCornerShape(15.dp))
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(83.dp)
        ) {

            Text(
                text = selectedOptionText,
                fontFamily = font_archivo,
                fontWeight = FontWeight.Medium,
                color = CardFieldText,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 15.dp)
            )

            if (expanded) {
                Image(
                    painter = painterResource(id = R.drawable.uparrow),
                    contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 13.5.dp, bottom = 13.5.dp, end = 15.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.down_arrow),
                    contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp, end = 15.dp)
                )
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}