package com.salesforce.loyalty.mobile.myntorewards.views.onboarding

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.BuildConfig
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.CardFieldText
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.DropDownMenuBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple90
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.DEFAULT_SAMPLE_APP_FORMAT
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_APP_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.SAMPLE_APP_DATE_FORMAT_DDLLLYYY
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.SAMPLE_APP_DATE_FORMAT_DDMMYYYY
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.DatePreferencesManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.LogoutState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import com.salesforce.loyalty.mobile.sources.PrefHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MoreOptions(
    onBoardingModel: OnBoardingViewModelAbstractInterface,
    navHostController: NavHostController
) {
    var openBottomsheet by remember { mutableStateOf(false) }

    val bottomSheetScaffoldState = androidx.compose.material.rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    if (openBottomsheet) {
        openBottomSheet()
    }
    val closeBottomSheet = {
        //showBottomBar(true)
        keyboardController?.hide()
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }
    androidx.compose.material.BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            Dateformatepopup() {
                openBottomsheet = false
                closeBottomSheet()
                //popupControlQRCOde = false
                //blurBG(AppConstants.NO_BLUR_BG)
            }
        },
        sheetShape = RoundedCornerShape(
            AppConstants.POPUP_ROUNDED_CORNER_SIZE,
            AppConstants.POPUP_ROUNDED_CORNER_SIZE,
            0.dp,
            0.dp
        ),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
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

            MoreOptionList(onBoardingModel, navHostController) {
                openBottomsheet = true
            }
        }
    }

}

@Composable
fun MoreOptionList(
    onBoardingModel: OnBoardingViewModelAbstractInterface,
    navHostController: NavHostController,
    openBottomSheet: (bottomsheetType: String) -> Unit
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
            ) {}
            AddMoreOption(
                imageRes = R.drawable.ic_addresses,
                textRes = R.string.header_label_addresses
            ) {}
            AddMoreOption(
                imageRes = R.drawable.ic_preference,
                textRes = R.string.header_label_payment_methods
            ) {}
            AddMoreOption(
                imageRes = R.drawable.ic_notification,
                textRes = R.string.header_label_orders
            ) {}
            ReceiptOption(navHostController)
            AddMoreOption(
                imageRes = R.drawable.ic_quote,
                textRes = R.string.header_label_support
            ) {}
            AddMoreOption(
                imageRes = R.drawable.ic_quote,
                textRes = R.string.header_label_dates
            ) {
                openBottomSheet("Date Formate")
            }
            AddMoreOption(
                imageRes = R.drawable.ic_favorite,
                textRes = R.string.header_label_favourite
            ) {}
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
    val appVersionValue =
        stringResource(id = R.string.label_app_version) + " " + BuildConfig.VERSION_NAME
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
fun AddMoreOption(imageRes: Int, textRes: Int, optionClicked: () -> Unit) {
    Divider(color = VibrantPurple90, thickness = Dp.Hairline)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 24.dp, bottom = 24.dp)
            .background(Color.White)
            .clickable {
                optionClicked()
            },
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
fun LogoutOption(onBoardingModel: OnBoardingViewModelAbstractInterface) {
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

@Composable
fun Dateformatepopup(closePopup: () -> Unit) {
    // openPopup(true)
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(
                MyProfileScreenBG,
                shape = RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE)
            ),
        horizontalAlignment = Alignment.Start
    ) {

        DateFormatHeader()
        {
            closePopup()
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Your Date will be applicable for app",
            fontFamily = font_sf_pro,
            color = LightBlack,
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 16.dp, end = 24.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            DateFormatDropDownBox()
        }
        Spacer(modifier = Modifier.height(50.dp))
        CloseDateFormatButton {
            closePopup()
        }

        Spacer(modifier = Modifier.height(55.dp))

    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DateFormatDropDownBox() {
    val options = listOf(
        DEFAULT_SAMPLE_APP_FORMAT,
        SAMPLE_APP_DATE_FORMAT_DDLLLYYY,
        SAMPLE_APP_DATE_FORMAT_DDMMYYYY
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    val context: Context = LocalContext.current
    val preferencesManager = remember { DatePreferencesManager(context) }
    val data = preferencesManager.getData(KEY_APP_DATE, DEFAULT_SAMPLE_APP_FORMAT)
    selectedOptionText = data


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
        ) {

            Text(
                text = selectedOptionText,
                fontFamily = font_archivo,
                fontWeight = FontWeight.SemiBold,
                color = CardFieldText,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(start = 12.dp, top = 20.dp, bottom = 20.dp, end = 15.dp)
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
                        preferencesManager.saveData(KEY_APP_DATE, selectedOptionText)
                        Toast
                            .makeText(
                                context,
                                "date format: " + selectedOptionText + " applied succesfully across the app.",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }


    }
}

@Composable
fun DateFormatHeader(closePopup: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 18.dp)
    )

    {
        Text(
            text = stringResource(id = R.string.date_format),
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(start = 16.dp),
            textAlign = TextAlign.Start,
        )

        Image(
            painter = painterResource(id = R.drawable.close_popup_icon),
            contentDescription = stringResource(id = R.string.cd_close_popup),
            modifier = Modifier
                .width(16.dp)
                .height(16.dp)
                .clickable {
                    closePopup()
                },
            contentScale = ContentScale.FillWidth,
        )
    }

}


@Composable
fun CloseDateFormatButton(closePopup: () -> Unit) {

    Column(modifier = Modifier.padding(start = 32.dp, end = 32.dp)) {
        Button(
            modifier = Modifier
                .fillMaxWidth(), onClick = {
                closePopup()
            },
            colors = ButtonDefaults.buttonColors(VibrantPurple40),
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(
                text = stringResource(id = R.string.close_text),
                fontFamily = font_sf_pro,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
        }
    }
}