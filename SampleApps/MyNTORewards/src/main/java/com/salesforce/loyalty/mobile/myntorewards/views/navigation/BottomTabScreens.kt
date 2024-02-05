package com.salesforce.loyalty.mobile.myntorewards.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_GAME_ZONE
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.CheckOutFlowOrderSelectScreen
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderDetails
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.OrderPlacedUI
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.GameZoneScreen
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.scratchcard.ScratchCardView
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinWheelLandingPage
import com.salesforce.loyalty.mobile.myntorewards.views.home.HomeScreenLandingView
import com.salesforce.loyalty.mobile.myntorewards.views.home.VoucherFullScreen
import com.salesforce.loyalty.mobile.myntorewards.views.myprofile.MyProfileLandingView
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.BottomNavTabs
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.MyReferralsListScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ProfileViewScreen
import com.salesforce.loyalty.mobile.myntorewards.views.offers.MyPromotionScreen
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.MoreOptions
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.*
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun HomeScreenAndCheckOutFlowNavigation(
    bottomTabsNavController: NavController,
    profileModel: MembershipProfileViewModelInterface,
    promotionModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    onboardingModel: OnBoardingViewModelAbstractInterface,
    benefitViewModel: BenefitViewModelInterface,
    transactionViewModel: TransactionViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    scanningViewModel: ScanningViewModelInterface,
    gameViewModel: GameViewModel,
    showBottomBar: (bottomBarVisible: Boolean) -> Unit
) {
    val navCheckOutFlowController = rememberNavController()
    NavHost(
        navController = navCheckOutFlowController,
        startDestination = CheckOutFlowScreen.StartCheckoutFlowScreen.route
    )
    {

        composable(route = CheckOutFlowScreen.StartCheckoutFlowScreen.route) {
            showBottomBar(true)
            HomeScreenLandingView(
                bottomTabsNavController,
                navCheckOutFlowController,
                profileModel,
                promotionModel,
                voucherModel
            )
        }
        composable(route = CheckOutFlowScreen.OrderDetailScreen.route) {
            showBottomBar(false)
            CheckOutFlowOrderSelectScreen(navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.OrderAddressAndPaymentScreen.route) {
            showBottomBar(false)
            OrderDetails(
                navCheckOutFlowController,
                voucherModel,
                checkOutFlowViewModel,
                profileModel,
                gameViewModel
            )
        }
        composable(route = CheckOutFlowScreen.OrderConfirmationScreen.route) {
            showBottomBar(false)
            OrderPlacedUI(navCheckOutFlowController, checkOutFlowViewModel, profileModel)
        }
        composable(route = CheckOutFlowScreen.VoucherFullScreen.route) {
            showBottomBar(true)
            VoucherFullScreen(navCheckOutFlowController, voucherModel)
        }
        composable(route = MoreScreens.ReceiptListScreen.route) {
            showBottomBar(true)
            ReceiptsList(navCheckOutFlowController, scanningViewModel)
        }
        composable(route = MoreScreens.CaptureImageScreen.route) {
            showBottomBar(false)
            ImageCaptureScreen(navCheckOutFlowController, scanningViewModel)
        }
        composable(route = MoreScreens.ReceiptDetailScreen.route) {
            showBottomBar(true)
            ReceiptDetail(navCheckOutFlowController, scanningViewModel)
        }
        composable(route = MoreScreens.ScannedReceiptScreen.route) {
            showBottomBar(false)
            ShowScannedReceiptScreen(navCheckOutFlowController, scanningViewModel)
        }
        composable(route = MoreScreens.ScannedCongratsScreen.route) {
            showBottomBar(false)
//            CongratulationsPopup(navCheckOutFlowController)
        }
        composable(route = MoreScreens.ScanningProgressScreen.route) {
            showBottomBar(false)
//            ScanningProgress(navCheckOutFlowController)
        }
        composable(route = MoreScreens.GameZoneScreen.route) {
            showBottomBar(true)
            GameZoneScreen(navCheckOutFlowController, gameViewModel)
        }
        composable(route = MoreScreens.ScratchCardScreen.route + "?gameParticipantRewardId={gameParticipantRewardId}",
            arguments = listOf(navArgument("gameParticipantRewardId"){
                type = NavType.StringType
            })) {
            showBottomBar(true)
            val gamePartRewardId = it.arguments?.getString("gameParticipantRewardId") ?: ""
            ScratchCardView(navCheckOutFlowController, gameViewModel, gamePartRewardId)
        }
        composable(route = MoreScreens.SpinWheelScreen.route + "?gameParticipantRewardId={gameParticipantRewardId}",
            arguments = listOf(navArgument("gameParticipantRewardId"){
                type = NavType.StringType
            })) {
            showBottomBar(true)
            val gamePartRewardId = it.arguments?.getString("gameParticipantRewardId") ?: ""
            SpinWheelLandingPage(navCheckOutFlowController, gameViewModel, gamePartRewardId)
        }
        composable(route = MoreScreens.GameCongratsScreen.route) {
            showBottomBar(false)
            val rewardType = navCheckOutFlowController.currentBackStackEntry?.arguments?.getString(AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_TYPE)?:""
            val rewardValue = navCheckOutFlowController.currentBackStackEntry?.arguments?.getString(AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_VALUE)?:""

            CongratulationsScreen(rewardType, rewardValue) {
                bottomTabsNavController.navigate(BottomNavTabs.More.route + "/$ROUTE_GAME_ZONE"){
                    popUpTo(0)
                }
            }
        }
        composable(route = MoreScreens.GameBetterLuckScreen.route) {
            showBottomBar(false)
            BetterLuckScreen {
                bottomTabsNavController.navigate(BottomNavTabs.More.route + "/$ROUTE_GAME_ZONE"){
                    popUpTo(0)
                }
            }
        }
        composable(route = MoreScreens.MyReferralsScreen.route) {
            MyReferralsListScreen(backAction =
            {
                navCheckOutFlowController.popBackStack()
            }) {
                showBottomBar(it)
            }
        }
    }
}


@Composable
fun PromotionScreenAndCheckOutFlowNavigation(
    bottomTabsNavController: NavController,
    promotionViewModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    profileModel: MembershipProfileViewModelInterface,
    gameViewModel: GameViewModel,
    showBottomBar: (bottomBarVisible: Boolean) -> Unit
) {
    val navCheckOutFlowController = rememberNavController()
    NavHost(
        navController = navCheckOutFlowController,
        startDestination = CheckOutFlowScreen.StartCheckoutFlowScreen.route
    )
    {

        composable(route = CheckOutFlowScreen.StartCheckoutFlowScreen.route) {


            showBottomBar(true)
            MyPromotionScreen(navCheckOutFlowController, promotionViewModel)


        }
        composable(route = CheckOutFlowScreen.OrderDetailScreen.route) {
            showBottomBar(false)
            CheckOutFlowOrderSelectScreen(navCheckOutFlowController)
        }
        composable(route = CheckOutFlowScreen.OrderAddressAndPaymentScreen.route) {
            showBottomBar(false)
            OrderDetails(
                navCheckOutFlowController,
                voucherModel,
                checkOutFlowViewModel,
                profileModel,
                gameViewModel
            )
        }
        composable(route = CheckOutFlowScreen.OrderConfirmationScreen.route) {
            showBottomBar(false)
            OrderPlacedUI(navCheckOutFlowController, checkOutFlowViewModel, profileModel)
        }
        composable(route = MoreScreens.GameZoneScreen.route) {
            showBottomBar(true)
            GameZoneScreen(navCheckOutFlowController, gameViewModel)
        }
        composable(route = MoreScreens.ScratchCardScreen.route + "?gameParticipantRewardId={gameParticipantRewardId}",
            arguments = listOf(navArgument("gameParticipantRewardId"){
                type = NavType.StringType
            })) {
            showBottomBar(true)
            val gamePartRewardId = it.arguments?.getString("gameParticipantRewardId") ?: ""
            ScratchCardView(navCheckOutFlowController, gameViewModel, gamePartRewardId)
        }
        composable(route = MoreScreens.SpinWheelScreen.route + "?gameParticipantRewardId={gameParticipantRewardId}",
            arguments = listOf(navArgument("gameParticipantRewardId"){
                type = NavType.StringType
            })) {
            showBottomBar(true)
            val gamePartRewardId = it.arguments?.getString("gameParticipantRewardId") ?: ""
            SpinWheelLandingPage(navCheckOutFlowController, gameViewModel, gamePartRewardId)
        }
        composable(route = MoreScreens.GameCongratsScreen.route) {
            showBottomBar(false)
            val rewardType = navCheckOutFlowController.currentBackStackEntry?.arguments?.getString(AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_TYPE)?:""
            val rewardValue = navCheckOutFlowController.currentBackStackEntry?.arguments?.getString(AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_VALUE)?:""

            CongratulationsScreen(rewardType, rewardValue ) {
                bottomTabsNavController.navigate(BottomNavTabs.More.route + "/$ROUTE_GAME_ZONE"){
                    popUpTo(0)
                }
            }
        }
        composable(route = MoreScreens.GameBetterLuckScreen.route) {
            showBottomBar(false)
            BetterLuckScreen {
                bottomTabsNavController.navigate(BottomNavTabs.More.route + "/$ROUTE_GAME_ZONE"){
                    popUpTo(0)
                }
            }
        }
        composable(route = MoreScreens.MyReferralsScreen.route) {
            MyReferralsListScreen(backAction =
            {
                navCheckOutFlowController.popBackStack()

            }) {
                showBottomBar(it)
            }
        }
    }
}

@Composable
fun MyProfileScreen(
    profileModel: MembershipProfileViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    benefitViewModel: BenefitViewModelInterface,
    transactionViewModel: TransactionViewModelInterface
) {
    val navProfileViewController = rememberNavController()
    NavHost(
        navController = navProfileViewController,
        startDestination = ProfileViewScreen.LandingProfileViewScreen.route
    )
    {

        composable(route = ProfileViewScreen.LandingProfileViewScreen.route) {
            MyProfileLandingView(
                navProfileViewController,
                profileModel,
                voucherModel,
                benefitViewModel,
                transactionViewModel
            )
        }
        composable(route = ProfileViewScreen.BenefitFullScreen.route) {
            MyProfileBenefitFullScreenView(navProfileViewController, benefitViewModel)
        }
        composable(route = ProfileViewScreen.TransactionFullScreen.route) {
            MyProfileTransactionFullScreenView(navProfileViewController, transactionViewModel)
        }
        composable(route = CheckOutFlowScreen.VoucherFullScreen.route) {
            VoucherFullScreen(navProfileViewController, voucherModel)
        }
    }


}


@Composable
fun RedeemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TextPurpleLightBG)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(id = R.string.screen_title_redeem),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MoreScreenNavigation(
    onboardingModel: OnBoardingViewModelAbstractInterface,
    scanningViewModel: ScanningViewModelInterface,
    gameViewModel: GameViewModel,
    voucherModel: VoucherViewModelInterface,
    showBottomBar: (bottomBarVisible: Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MoreScreens.MoreScreenOptions.route
    )
    {

        composable(route = MoreScreens.MoreScreenOptions.route) {
            showBottomBar(true)
            MoreOptions(onboardingModel, navController)
        }
        composable(route = MoreScreens.ReceiptListScreen.route) {
            showBottomBar(true)
            ReceiptsList(navController, scanningViewModel)
        }
        composable(route = MoreScreens.CaptureImageScreen.route) {
            showBottomBar(false)
            ImageCaptureScreen(navController, scanningViewModel)
        }
        composable(route = MoreScreens.ReceiptDetailScreen.route) {
            showBottomBar(true)
            ReceiptDetail(navController, scanningViewModel)
        }
        composable(route = MoreScreens.ScannedReceiptScreen.route) {
            showBottomBar(false)
            ShowScannedReceiptScreen(navController, scanningViewModel)
        }
        composable(route = MoreScreens.ScannedCongratsScreen.route) {
            showBottomBar(false)
//            CongratulationsPopup(navController)
        }
        composable(route = MoreScreens.ScanningProgressScreen.route) {
            showBottomBar(false)
//            ScanningProgress(navController)
        }
        composable(route = MoreScreens.GameZoneScreen.route) {
            GameZoneNavigation(gameViewModel = gameViewModel, voucherModel, showBottomBar = showBottomBar)
        }
        composable(route = MoreScreens.MyReferralsScreen.route) {
//            showBottomBar(true)
            MyReferralsListScreen(backAction = { navController.popBackStack() }) {
                showBottomBar(it)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun GameZoneNavigation(
    gameViewModel: GameViewModel,
    voucherModel: VoucherViewModelInterface,
    showBottomBar: (bottomBarVisible: Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MoreScreens.GameZoneScreen.route
    )
    {
        composable(route = MoreScreens.GameZoneScreen.route) {
            showBottomBar(true)
            GameZoneScreen(navController, gameViewModel)
        }
        composable(route = MoreScreens.ScratchCardScreen.route + "?gameParticipantRewardId={gameParticipantRewardId}",
            arguments = listOf(navArgument("gameParticipantRewardId"){
                type = NavType.StringType
            })) {
            showBottomBar(true)
            val gamePartRewardId = it.arguments?.getString("gameParticipantRewardId") ?: ""
            ScratchCardView(navController, gameViewModel, gamePartRewardId)
        }
        composable(route = MoreScreens.SpinWheelScreen.route + "?gameParticipantRewardId={gameParticipantRewardId}",
        arguments = listOf(navArgument("gameParticipantRewardId"){
            type = NavType.StringType
        })) {
            showBottomBar(true)
            val gamePartRewardId = it.arguments?.getString("gameParticipantRewardId") ?: ""
            SpinWheelLandingPage(navController, gameViewModel, gamePartRewardId)
        }
        composable(route = MoreScreens.GameCongratsScreen.route) {
            showBottomBar(false)

            val rewardType = navController.currentBackStackEntry?.arguments?.getString(AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_TYPE)?:""
            val rewardValue = navController.currentBackStackEntry?.arguments?.getString(AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_VALUE)?:""


            CongratulationsScreen(rewardType,rewardValue ) {
                navController.popBackStack(
                    MoreScreens.GameZoneScreen.route,
                    false
                )
            }
        }

        composable(route = CheckOutFlowScreen.VoucherFullScreen.route) {
            showBottomBar(true)
            VoucherFullScreen(navController, voucherModel)
        }

        composable(route = MoreScreens.GameBetterLuckScreen.route) {
            showBottomBar(false)
            BetterLuckScreen {
                navController.popBackStack(
                    MoreScreens.GameZoneScreen.route,
                    false
                )
            }
        }
        composable(route = MoreScreens.MyReferralsScreen.route) {
//            showBottomBar(true)
            MyReferralsListScreen(backAction = { navController.popBackStack() }) {
                showBottomBar(it)
            }
        }
    }
}