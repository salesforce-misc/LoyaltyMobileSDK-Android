package com.salesforce.loyalty.mobile.myntorewards.views

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.BottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.ViewPagerSupport
import com.salesforce.loyalty.mobile.myntorewards.utilities.ViewPagerSupport.ViewPagerSupport.screenTextID
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.myntorewards.views.adminmenu.SettingsMain
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.EnrollmentCongratulationsView
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.EnrollmentWebView
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.JoinWithTermsAndConditions
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch



@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun OnboardingScreenBox(navController: NavController,model: OnBoardingViewModelAbstractInterface) {

    var currentPopupState: BottomSheetType? by remember { mutableStateOf(null) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )

    // Declaring Coroutine scope
    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    var openAdminMenu by remember { mutableStateOf(false) }

    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    val closeBottomSheet = {
        keyboardController?.hide()
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            currentPopupState?.let { bottomSheetType ->
                OpenBottomSheetContent(
                    bottomSheetType = bottomSheetType,
                    navController = navController,
                    setBottomSheetState = {
                        currentPopupState = it
                    }, closeSheet = { closeBottomSheet() },
                    model
                )
            }
        },
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Box(modifier = Modifier.fillMaxSize())
        {
            var tapCount = 0

            //setting up swipe pager and images accordingly
            val pagerState = rememberPagerState()
            HorizontalPager(count = 3, state = pagerState) { page ->
                Image(
                    painter = painterResource(id = ViewPagerSupport.imageID(page)),
                    contentDescription = stringResource(R.string.cd_onboard_screen_onboard_image),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(""+ViewPagerSupport.imageID(page))
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                tapCount++
                                Logger.d("Onboarding", "Tap detected $tapCount")
                                if (tapCount == AppConstants.TAP_COUNT_OPEN_ADMIN_SETTINGS) {
                                    //navigate to admin settings
                                    tapCount = 0
                                    Logger.d(
                                        "Onboarding",
                                        "Tap detected navigate to admin settings"
                                    )
                                    openAdminMenu = true
                                }
                            })
                        },
                    contentScale = ContentScale.FillWidth,
                )
            }

            Image(
                painter = painterResource(id = R.drawable.screen_bottom_black_fading),
                contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomEnd)
                    .padding(start = 24.dp, end = 24.dp),
                verticalArrangement = Arrangement.Bottom
            )
            {

                Image(
                    painter = painterResource(id = R.drawable.application_logo),
                    contentDescription = stringResource(id = R.string.cd_app_logo)
                )

                //Updating the screen text dynamically as page swipe
                val stringTextID = remember { mutableStateOf(R.string.onboard_screen_text_1) }
                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        stringTextID.value = screenTextID(page)
                    }
                }
                Text(
                    text = stringResource(stringTextID.value),
                    fontFamily = font_sf_pro,
                    color = Color.White,
                    fontSize = 34.sp
                )

                //Swipe indicator
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    activeColor = Color.White,
                    inactiveColor = Color.Gray,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(48.dp))
                JoinLoginButtonBox(bottomSheetScaffoldState, coroutineScope) {
                    currentPopupState = it
                }
                Spacer(modifier = Modifier.height(55.dp))
            }
            if (openAdminMenu) {
                openAdminMenu = false
                currentPopupState = BottomSheetType.SETTINGS
                openBottomSheet()
            }
        }
    }
}

@Composable
fun OpenBottomSheetContent(
    bottomSheetType: BottomSheetType,
    navController: NavController,
    setBottomSheetState: (bottomSheetState: BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
    model: OnBoardingViewModelAbstractInterface
) {
    val activity = LocalContext.current as Activity
    when (bottomSheetType) {
        BottomSheetType.POPUP_LOGIN -> {
            LoginUI(
                navController = navController,
                openPopup = { setBottomSheetState(it) },
                closeSheet = closeSheet,
                model

            )
        }
        BottomSheetType.POPUP_CONGRATULATIONS -> {
            EnrollmentCongratulationsView(navController, closeSheet = closeSheet, openPopup = { setBottomSheetState(it) })
        }
        BottomSheetType.SETTINGS -> {
            SettingsMain(
                closeSheet = {
                    /* Restart the activity after making changes to the connected app settings, so
                    that the Retrofit instances gets re-initialized with the correct selected
                    connected app setting.*/
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                }
            )
        }
        BottomSheetType.POPUP_SELF_REGISTER -> {
            EnrollmentWebView(
                model = model,
                openPopup = { setBottomSheetState(it) },
                closeSheet = closeSheet)
        }
        BottomSheetType.POPUP_ENROLLMENT -> {
            JoinWithTermsAndConditions(
                closeSheet = closeSheet,
                navController = navController,
                model = model
            )
        }
    }
}