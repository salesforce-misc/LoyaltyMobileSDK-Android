package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen

@Composable
fun MainScreenStart(profileModel: MembershipProfileViewModelInterface,
                    promotionModel: MyPromotionViewModelInterface,
                    voucherModel: VoucherViewModelInterface,
                    onboardingModel: OnBoardingViewModelAbstractInterface,
                    benefitModel: BenefitViewModelInterface,
                    transactionModel: TransactionViewModelInterface,
                    checkoutFlowModel: CheckOutFlowViewModelInterface,
                    scanningViewModel: ScanningViewModelInterface, gameViewModel: GameViewModelInterface) {

    Navigation(
        profileModel,
        promotionModel,
        voucherModel,
        onboardingModel,
        benefitModel,
        transactionModel,
        checkoutFlowModel,
        scanningViewModel,
        gameViewModel
    )
}

@Composable
fun Navigation(
    profileModel: MembershipProfileViewModelInterface,
    promotionModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    onboardingModel: OnBoardingViewModelAbstractInterface,
    benefitViewModel: BenefitViewModelInterface,
    transactionViewModel: TransactionViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    scanningViewModel: ScanningViewModelInterface,
    gameViewModel: GameViewModelInterface

) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.OnboardingScreen.route)
    {

        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreenBox(navController, onboardingModel)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeTabScreen(profileModel,promotionModel,voucherModel, onboardingModel, benefitViewModel, transactionViewModel, checkOutFlowViewModel, scanningViewModel, gameViewModel)
        }
    }
}