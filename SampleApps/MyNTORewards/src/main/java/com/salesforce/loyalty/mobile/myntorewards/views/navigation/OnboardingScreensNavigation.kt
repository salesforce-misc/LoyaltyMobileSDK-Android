package com.salesforce.loyalty.mobile.myntorewards.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScreenStart(profileModel: MembershipProfileViewModelInterface,
                    badgeViewModel: BadgeViewModelInterface,
                    promotionModel: MyPromotionViewModelInterface,
                    voucherModel: VoucherViewModelInterface,
                    onboardingModel: OnBoardingViewModelAbstractInterface,
                    benefitModel: BenefitViewModelInterface,
                    transactionModel: TransactionViewModelInterface,
                    checkoutFlowModel: CheckOutFlowViewModelInterface,
                    scanningViewModel: ScanningViewModelInterface,
                    gameViewModel: GameViewModel = hiltViewModel(),
                    referralViewModel: MyReferralsViewModel = hiltViewModel()) {

    Navigation(
        profileModel,
        badgeViewModel,
        promotionModel,
        voucherModel,
        onboardingModel,
        benefitModel,
        transactionModel,
        checkoutFlowModel,
        scanningViewModel,
        gameViewModel,
        referralViewModel
    )
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Navigation(
    profileModel: MembershipProfileViewModelInterface,
    badgeViewModel: BadgeViewModelInterface,
    promotionModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface,
    onboardingModel: OnBoardingViewModelAbstractInterface,
    benefitViewModel: BenefitViewModelInterface,
    transactionViewModel: TransactionViewModelInterface,
    checkOutFlowViewModel: CheckOutFlowViewModelInterface,
    scanningViewModel: ScanningViewModelInterface,
    gameViewModel: GameViewModel,
    referralViewModel: MyReferralsViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.OnboardingScreen.route)
    {

        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreenBox(navController, onboardingModel)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeTabScreen(profileModel,badgeViewModel, promotionModel,voucherModel, onboardingModel, benefitViewModel, transactionViewModel, checkOutFlowViewModel, scanningViewModel, gameViewModel, referralViewModel)
        }
    }
}