package com.salesforce.loyalty.mobile.myntorewards.views.onboarding

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.BuildConfig
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple90
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.LogoutState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import com.salesforce.loyalty.mobile.sources.PrefHelper


@Composable
fun MoreOptions(
    onBoardingModel: OnBoardingViewModelAbstractInterface,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        val context = LocalContext.current
        val memberJson =
            PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
        val member = memberJson?.let {
            Gson().fromJson(memberJson, CommunityMemberModel::class.java)
        }
        val firstName = member?.let { member.firstName } ?: ""
        val lastName = member?.let { member.lastName } ?: ""

        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.user_pic),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = "$firstName $lastName",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        MoreOptionList(onBoardingModel, navHostController)
    }
}

@Composable
fun MoreOptionList(
    onBoardingModel: OnBoardingViewModelAbstractInterface,
    navHostController: NavHostController
) {

    val logoutState by onBoardingModel.logoutStateLiveData.observeAsState(LogoutState.LOGOUT_DEFAULT_EMPTY) // collecting livedata as state
    var isInProgress by remember { mutableStateOf(false) }
    when (logoutState) {
        LogoutState.LOGOUT_SUCCESS -> {
            isInProgress = false
            val activity = LocalContext.current as Activity
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
        LogoutState.LOGOUT_IN_PROGRESS -> {
            isInProgress = true
        }
        else -> {}
    }
    Spacer(modifier = Modifier.height(24.dp))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {

            AddMoreOption(
                imageRes = R.drawable.ic_user_account,
                textRes = R.string.header_label_account
            )
            AddMoreOption(
                imageRes = R.drawable.ic_addresses,
                textRes = R.string.header_label_addresses
            )
            AddMoreOption(
                imageRes = R.drawable.ic_preference,
                textRes = R.string.header_label_payment_methods
            )
            AddMoreOption(
                imageRes = R.drawable.ic_notification,
                textRes = R.string.header_label_orders
            )
            ReceiptOption(navHostController)
            AddMoreOption(
                imageRes = R.drawable.ic_favorite,
                textRes = R.string.header_label_favourite
            )
            LogoutOption(onBoardingModel)
            AppVersionHolder()
        }
        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun AppVersionHolder() {
    val appVersionValue = stringResource(id = R.string.label_app_version) + " " + BuildConfig.VERSION_NAME
    Text(
        text = appVersionValue,
        fontFamily = font_sf_pro,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
        modifier = Modifier.padding(top = 16.dp),
        fontSize = 14.sp
    )
}

@Composable
fun AddMoreOption(imageRes: Int, textRes: Int){
    Divider(color = VibrantPurple90, thickness = Dp.Hairline)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 24.dp, bottom = 24.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(textRes),
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .padding(4.dp),
            contentScale = ContentScale.Inside
        )
        Text(
            text = stringResource(id = textRes),
            fontFamily = font_sf_pro,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 18.sp
        )
    }

    Divider(color = VibrantPurple90, thickness = Dp.Hairline)
}

@Composable
fun ReceiptOption(navHostController: NavHostController) {
    Divider(color = VibrantPurple90, thickness = Dp.Hairline)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 24.dp, bottom = 24.dp)
            .background(Color.White)
            .clickable {
                navHostController.navigate(MoreScreens.ReceiptListScreen.route)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.receipt_menu),
            contentDescription = stringResource(R.string.header_label_receipt),
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .padding(4.dp),
            contentScale = ContentScale.Inside
        )
        Text(
            text = stringResource(id = R.string.header_label_receipt),
            fontFamily = font_sf_pro,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 18.sp
        )
    }

    Divider(color = VibrantPurple90, thickness = Dp.Hairline)
}

@Composable
fun LogoutOption(onBoardingModel: OnBoardingViewModelAbstractInterface){
    val context = LocalContext.current
    Divider(color = VibrantPurple90, thickness = Dp.Hairline)

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 24.dp, bottom = 24.dp)
            .background(Color.White)
            .clickable(indication = null, interactionSource = interactionSource) {
                onBoardingModel.logoutAndClearAllSettings(context)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_logout_24),
            contentDescription = stringResource(R.string.label_logout),
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .padding(4.dp),
            contentScale = ContentScale.Inside
        )
        Text(
            text = stringResource(id = R.string.label_logout),
            fontFamily = font_sf_pro,
            fontWeight = FontWeight.Normal,
            color = VibrantPurple40,
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 18.sp
        )
    }

    Divider(color = VibrantPurple90, thickness = Dp.Hairline)
}