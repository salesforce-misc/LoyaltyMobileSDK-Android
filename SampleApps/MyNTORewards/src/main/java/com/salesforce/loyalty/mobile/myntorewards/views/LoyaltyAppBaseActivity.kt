package com.salesforce.loyalty.mobile.myntorewards.views


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger

//Main Activity Application Entry Point
class LoyaltyAppBaseActivity : ComponentActivity() {
    private val TAG = LoyaltyAppBaseActivity::class.java.simpleName
    val model: OnboardingScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        ForceAuthManager.getInstance(applicationContext)

        val loginSuccess = PrefHelper.customPrefs(applicationContext)
            .get(AppConstants.KEY_LOGIN_SUCCESSFUL, false)
        setContent {
            if (loginSuccess == true) {
                val onboardingModel: OnboardingScreenViewModel = viewModel()
                val profileModel: MembershipProfileViewModel = viewModel()
                val promotionModel: MyPromotionViewModel = viewModel()
                val voucherModel: VoucherViewModel = viewModel()
                val benefitModel: MembershipBenefitViewModel = viewModel()
                val transactionModel: TransactionsViewModel = viewModel()
                val checkoutFlowModel: CheckOutFlowViewModel = viewModel()  //fetching reference of viewmodel
                HomeTabScreen(profileModel, promotionModel, voucherModel, onboardingModel, benefitModel, transactionModel, checkoutFlowModel)
            } else {
                MainScreenStart()
            }
        }
        observeSessionExpiry()
        observeLoginStatus(model)
    }

    private fun observeSessionExpiry() {
        ForceAuthManager.forceAuthManager.authenticationStatusLiveData.observe(this) { status ->
            if (ForceAuthManager.AuthenticationStatus.UNAUTHENTICATED == status) {
                Logger.d(TAG, "observeSessionExpiry() status: $status")
                model.logoutAndClearAllSettings(applicationContext)
            }
        }
    }

    private fun observeLoginStatus(model: OnboardingScreenViewModel) {
        model.logoutStateLiveData.observe(this) { logoutState ->
            run {
                if (LogoutState.LOGOUT_SUCCESS == logoutState) {
                    Logger.d(TAG, "observeLoginStatus() logout success")
                    setContent {
                        rememberNavController().clearBackStack(0)
                        MainScreenStart()
                    }
                }
            }
        }
    }
}

