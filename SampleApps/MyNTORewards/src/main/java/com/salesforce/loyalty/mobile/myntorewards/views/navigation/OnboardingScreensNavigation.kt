package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipProfileViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OnboardingScreenViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.VoucherViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MembershipProfileViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MyPromotionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.VoucherViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen

@Composable
fun MainScreenStart() {
    val model: OnboardingScreenViewModel = viewModel()
    val profileModel: MembershipProfileViewModel = viewModel()
    val promotionModel:  MyPromotionViewModel = viewModel()
    val voucherModel: VoucherViewModel = viewModel()
    Navigation(model,profileModel, promotionModel, voucherModel)
}

@Composable
fun Navigation(
    model: OnBoardingViewModelAbstractInterface,
    profileModel: MembershipProfileViewModelInterface,
    promotionModel: MyPromotionViewModelInterface,
    voucherModel: VoucherViewModelInterface
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.OnboardingScreen.route)
    {

        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreenBox(navController, model)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeTabScreen(profileModel,promotionModel,voucherModel )
        }
    }
}