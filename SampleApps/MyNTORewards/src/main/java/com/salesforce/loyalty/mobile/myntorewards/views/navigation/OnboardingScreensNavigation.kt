package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.checkout.CheckoutManager
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.*
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
    val checkoutManager: CheckoutManager = CheckoutManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )

    val activity = LocalContext.current as LoyaltyAppBaseActivity

    val onboardingModel: OnboardingScreenViewModel= ViewModelProvider(activity, OnboardingScreenViewModelFactory(loyaltyAPIManager)).get(OnboardingScreenViewModel::class.java)
    val profileModel: MembershipProfileViewModel= ViewModelProvider(activity, ProfileViewModelFactory(loyaltyAPIManager)).get(MembershipProfileViewModel::class.java)
    val promotionModel: MyPromotionViewModel= ViewModelProvider(activity, MyPromotionViewModelFactory(loyaltyAPIManager)).get(MyPromotionViewModel::class.java)
    val voucherModel: VoucherViewModel= ViewModelProvider(activity, VoucherViewModelFactory(loyaltyAPIManager)).get(VoucherViewModel::class.java)
    val benefitModel: MembershipBenefitViewModel= ViewModelProvider(activity, BenefitViewModelFactory(loyaltyAPIManager)).get(MembershipBenefitViewModel::class.java)
    val transactionModel: TransactionsViewModel= ViewModelProvider(activity, TransactionViewModelFactory(loyaltyAPIManager)).get(TransactionsViewModel::class.java)
    val checkoutFlowModel: CheckOutFlowViewModel= ViewModelProvider(activity, CheckOutFlowViewModelFactory(checkoutManager)).get(CheckOutFlowViewModel::class.java)

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