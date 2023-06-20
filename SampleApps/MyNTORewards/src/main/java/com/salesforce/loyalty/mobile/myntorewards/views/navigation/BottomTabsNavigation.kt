package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_HOME_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.BottomNavTabs

@Composable
fun HomeTabScreen(profileModel: MembershipProfileViewModelInterface,
                  promotionModel: MyPromotionViewModelInterface,
                  voucherModel: VoucherViewModelInterface,
                  onboardingModel: OnBoardingViewModelAbstractInterface,
                  benefitViewModel: BenefitViewModelInterface,
                  transactionViewModel: TransactionViewModelInterface,
                  checkOutFlowViewModel: CheckOutFlowViewModelInterface
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
                .background(TextPurpleLightBG).testTag(TEST_TAG_HOME_SCREEN)
        ) {
            TabNavigation(bottomTabsNavController, profileModel,promotionModel,voucherModel, onboardingModel, benefitViewModel, transactionViewModel,checkOutFlowViewModel) {
                bottomBarState.value = it
            }
        }
    }
}

@Composable
fun TabNavigation(
    bottomTabsNavController: NavHostController,
    profileModel: MembershipProfileViewModelInterface,
    promotionModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    onboardingModel: OnBoardingViewModelAbstractInterface,
    benefitViewModel: BenefitViewModelInterface,
    transactionViewModel: TransactionViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    showBottomBar: (bottomBarVisible: Boolean) -> Unit
) {

    NavHost(navController = bottomTabsNavController, startDestination = BottomNavTabs.Home.route)
    {
        composable(route = BottomNavTabs.Home.route) {

            HomeScreenAndCheckOutFlowNavigation(bottomTabsNavController, profileModel,promotionModel,voucherModel, onboardingModel, benefitViewModel, transactionViewModel, checkOutFlowViewModel) {
                showBottomBar(it)
            }
        }
        composable(route = BottomNavTabs.MyOffers.route) {
            PromotionScreenAndCheckOutFlowNavigation(promotionModel, voucherModel, checkOutFlowViewModel) {
                showBottomBar(it)
            }
        }
        composable(route = BottomNavTabs.MyProfile.route) {
            MyProfileScreen(profileModel,voucherModel, benefitViewModel, transactionViewModel)
        }
        //part of UX but not part of MVP
        /*  composable(route = BottomNavTabs.Redeem.route) {
              RedeemScreen()
          }*/
        composable(route = BottomNavTabs.More.route) {
            MoreScreen(onboardingModel) {
                showBottomBar(it)
            }
        }
    }
}