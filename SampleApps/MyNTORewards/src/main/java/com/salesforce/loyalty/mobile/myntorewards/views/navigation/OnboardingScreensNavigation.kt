package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.BenefitViewModelFactory
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.TransactionViewModelFactory
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.VoucherViewModelFactory
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyClient

@Composable
fun MainScreenStart() {
    val mInstanceUrl =
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        mInstanceUrl,
        LoyaltyClient(ForceAuthManager.forceAuthManager, mInstanceUrl)
    )

    val activity = LocalContext.current as LoyaltyAppBaseActivity

    val onboardingModel: OnboardingScreenViewModel = viewModel()
    val profileModel: MembershipProfileViewModel = viewModel()
    val promotionModel:  MyPromotionViewModel = viewModel()
    val voucherModel: VoucherViewModel= ViewModelProvider(activity, VoucherViewModelFactory(loyaltyAPIManager)).get(VoucherViewModel::class.java)
    val benefitModel: MembershipBenefitViewModel= ViewModelProvider(activity, BenefitViewModelFactory(loyaltyAPIManager)).get(MembershipBenefitViewModel::class.java)
    val transactionModel: TransactionsViewModel= ViewModelProvider(activity, TransactionViewModelFactory(loyaltyAPIManager)).get(TransactionsViewModel::class.java)
    val checkoutFlowModel: CheckOutFlowViewModel = viewModel()  //fetching reference of viewmodel
    Navigation(

        profileModel,
        promotionModel,
        voucherModel,
        onboardingModel,
        benefitModel,
        transactionModel,
        checkoutFlowModel
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
    checkOutFlowViewModel: CheckOutFlowViewModelInterface

) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.OnboardingScreen.route)
    {

        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreenBox(navController, onboardingModel)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeTabScreen(profileModel,promotionModel,voucherModel, onboardingModel, benefitViewModel, transactionViewModel, checkOutFlowViewModel)
        }
    }
}