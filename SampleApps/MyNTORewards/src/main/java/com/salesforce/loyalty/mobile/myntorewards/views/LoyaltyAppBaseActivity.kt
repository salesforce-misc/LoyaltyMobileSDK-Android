package com.salesforce.loyalty.mobile.myntorewards.views


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.checkout.CheckoutManager
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.*
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyClient

//Main Activity Application Entry Point
class LoyaltyAppBaseActivity : ComponentActivity() {
    private val TAG = LoyaltyAppBaseActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        val forceAuthManager = ForceAuthManager(applicationContext)
        val loginSuccess = PrefHelper.customPrefs(applicationContext)
            .get(AppConstants.KEY_LOGIN_SUCCESSFUL, false)

        val mInstanceUrl =
            forceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
        val loyaltyAPIManager = LoyaltyAPIManager(
            auth = forceAuthManager,
            instanceUrl = mInstanceUrl,
            loyaltyClient = LoyaltyClient(forceAuthManager, mInstanceUrl)
        )
        val onboardingModel: OnboardingScreenViewModel =
            ViewModelProvider(
                this,
                OnboardingScreenViewModelFactory(loyaltyAPIManager, forceAuthManager)
            ).get(
                OnboardingScreenViewModel::class.java
            )
        val profileModel: MembershipProfileViewModel =
            ViewModelProvider(this, ProfileViewModelFactory(loyaltyAPIManager)).get(
                MembershipProfileViewModel::class.java
            )
        val promotionModel: MyPromotionViewModel =
            ViewModelProvider(this, MyPromotionViewModelFactory(loyaltyAPIManager)).get(
                MyPromotionViewModel::class.java
            )
        val voucherModel: VoucherViewModel = ViewModelProvider(
            this,
            VoucherViewModelFactory(loyaltyAPIManager)
        ).get(VoucherViewModel::class.java)
        val benefitModel: MembershipBenefitViewModel =
            ViewModelProvider(this, BenefitViewModelFactory(loyaltyAPIManager)).get(
                MembershipBenefitViewModel::class.java
            )
        val transactionModel: TransactionsViewModel =
            ViewModelProvider(this, TransactionViewModelFactory(loyaltyAPIManager)).get(
                TransactionsViewModel::class.java
            )

        val checkoutManager: CheckoutManager = CheckoutManager(
            forceAuthManager,
            forceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
        )
        val checkoutFlowModel: CheckOutFlowViewModel =
            ViewModelProvider(this, CheckOutFlowViewModelFactory(checkoutManager)).get(
                CheckOutFlowViewModel::class.java
            )

        setContent {
            if (loginSuccess == true) {
                HomeTabScreen(
                    profileModel,
                    promotionModel,
                    voucherModel,
                    onboardingModel,
                    benefitModel,
                    transactionModel,
                    checkoutFlowModel
                )
            } else {
                MainScreenStart(
                    profileModel,
                    promotionModel,
                    voucherModel,
                    onboardingModel,
                    benefitModel,
                    transactionModel,
                    checkoutFlowModel
                )
            }
        }
        observeSessionExpiry(onboardingModel, forceAuthManager)
    }

    private fun observeSessionExpiry(model: OnboardingScreenViewModel, forceAuthManager: ForceAuthManager) {
        forceAuthManager.authenticationStatusLiveData.observe(this) { status ->
            if (ForceAuthManager.AuthenticationStatus.UNAUTHENTICATED == status) {
                Logger.d(TAG, "observeSessionExpiry() status: $status")
                model.logoutAndClearAllSettings(applicationContext)
            }
        }
    }
}

