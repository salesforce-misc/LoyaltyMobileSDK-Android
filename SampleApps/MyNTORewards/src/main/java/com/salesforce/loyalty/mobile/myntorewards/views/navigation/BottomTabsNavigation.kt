package com.salesforce.loyalty.mobile.myntorewards.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_GAME_ZONE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_HOME_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.BottomNavTabs
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

@RequiresApi(Build.VERSION_CODES.P)
@Composable

fun HomeTabScreen(
    profileModel: MembershipProfileViewModelInterface,
    badgeViewModel:BadgeViewModelInterface,
    promotionModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    onboardingModel: OnBoardingViewModelAbstractInterface,
    benefitViewModel: BenefitViewModelInterface,
    transactionViewModel: TransactionViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    scanningViewModel: ScanningViewModelInterface,
    gameViewModel: GameViewModel = hiltViewModel(),
    referralViewModel: MyReferralsViewModel

) {
    val bottomTabsNavController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    Scaffold(
        Modifier.background(TextPurpleLightBG),
        bottomBar = { BottomNavigationUI(bottomTabsNavController, bottomBarState) }

    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(TextPurpleLightBG)
                .testTag(TEST_TAG_HOME_SCREEN)
        ) {

            TabNavigation(bottomTabsNavController, profileModel, badgeViewModel, promotionModel,voucherModel, onboardingModel, benefitViewModel, transactionViewModel,checkOutFlowViewModel, scanningViewModel, gameViewModel, referralViewModel) {

                bottomBarState.value = it
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun TabNavigation(
    bottomTabsNavController: NavHostController,
    profileModel: MembershipProfileViewModelInterface,
    badgeViewModel:BadgeViewModelInterface,
    promotionModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    onboardingModel: OnBoardingViewModelAbstractInterface,
    benefitViewModel: BenefitViewModelInterface,
    transactionViewModel: TransactionViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    scanningViewModel: ScanningViewModelInterface,
    gameViewModel: GameViewModel,
    referralViewModel: MyReferralsViewModel,
    showBottomBar: (bottomBarVisible: Boolean) -> Unit
) {

    NavHost(navController = bottomTabsNavController, startDestination = BottomNavTabs.Home.route)
    {
        composable(route = BottomNavTabs.Home.route) {

            HomeScreenAndCheckOutFlowNavigation(bottomTabsNavController, profileModel,promotionModel,voucherModel, onboardingModel, benefitViewModel, transactionViewModel, checkOutFlowViewModel, scanningViewModel, gameViewModel, referralViewModel) {
                showBottomBar(it)
            }
        }
        composable(route = BottomNavTabs.MyOffers.route) {
            PromotionScreenAndCheckOutFlowNavigation(bottomTabsNavController, promotionModel, voucherModel, checkOutFlowViewModel, profileModel, gameViewModel, referralViewModel) {
                showBottomBar(it)
            }
        }
        composable(route = BottomNavTabs.MyProfile.route) {
            MyProfileScreen(profileModel, badgeViewModel, voucherModel, benefitViewModel, transactionViewModel)
        }
        //part of UX but not part of MVP
        /*  composable(route = BottomNavTabs.Redeem.route) {
              RedeemScreen()
          }*/
        composable(route = BottomNavTabs.More.route) {
            MoreScreenNavigation( onboardingModel, scanningViewModel, gameViewModel, voucherModel){
                showBottomBar(it)
            }
        }
        composable(route = BottomNavTabs.More.route+ "/{openGameZone}") {
            val gameZone = it.arguments?.getString("openGameZone")
            if (gameZone.equals(ROUTE_GAME_ZONE)) {
                GameZoneNavigation(gameViewModel = gameViewModel, voucherModel) { showBottomBar(it) }
            }
        }
    }
}