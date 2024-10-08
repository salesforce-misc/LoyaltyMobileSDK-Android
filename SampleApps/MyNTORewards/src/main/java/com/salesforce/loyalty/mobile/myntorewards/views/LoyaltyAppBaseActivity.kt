package com.salesforce.loyalty.mobile.myntorewards.views

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.badge.LoyaltyBadgeManager
import com.salesforce.loyalty.mobile.myntorewards.checkout.CheckoutManager
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.ReceiptScanningManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.*
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyClient
import dagger.hilt.android.AndroidEntryPoint

//Main Activity Application Entry Point
@AndroidEntryPoint
class LoyaltyAppBaseActivity : ComponentActivity() {
    private val TAG = LoyaltyAppBaseActivity::class.java.simpleName

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH

        val forceAuthManager = ForceAuthManager(applicationContext)
        val loginSuccess = PrefHelper.customPrefs(applicationContext)
            .get(AppConstants.KEY_LOGIN_SUCCESSFUL, false)

        val mInstanceUrl =
            forceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
        val mLoyaltyProgramName = forceAuthManager.getLoyaltyProgramName() ?: AppSettings.LOYALTY_PROGRAM_NAME
        val loyaltyAPIManager = LoyaltyAPIManager(
            auth = forceAuthManager,
            instanceUrl = mInstanceUrl,
            mLoyaltyProgramName,
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

        val receiptManager: ReceiptScanningManager = ReceiptScanningManager(
            forceAuthManager,
            forceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
        )
        val scanningViewModel: ScanningViewModel =
            ViewModelProvider(this, ScanningViewModelFactory(receiptManager)).get(
                ScanningViewModel::class.java
            )
        val badgeManager = LoyaltyBadgeManager(
            forceAuthManager,
            forceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
        )
        val badgeViewModel: BadgeViewModel =
            ViewModelProvider(this, BadgeViewModelFactory(badgeManager)).get(
                BadgeViewModel::class.java
            )


        /*        val gameViewModel: GameViewModel =
                    ViewModelProvider(this, GameViewModelFactory(loyaltyAPIManager)).get(
                        GameViewModel::class.java
                    )*/
        setContent {
            val referralViewModel: MyReferralsViewModel = hiltViewModel()
            if (loginSuccess == true) {

               
                //SpinWheelLandingPage(loyaltyAPIManager)
                HomeTabScreen(
                    profileModel,
                    badgeViewModel,
                    promotionModel,
                    voucherModel,
                    onboardingModel,
                    benefitModel,
                    transactionModel,
                    checkoutFlowModel,
                    scanningViewModel,
                    referralViewModel = referralViewModel
                )
            } else {
                MainScreenStart(
                    profileModel,
                    badgeViewModel,
                    promotionModel,
                    voucherModel,
                    onboardingModel,
                    benefitModel,
                    transactionModel,
                    checkoutFlowModel,
                    scanningViewModel
                )
                // Added for testing purpose.
                //ScratchCardView(loyaltyAPIManager)


            }
        }
        observeSessionExpiry(onboardingModel, forceAuthManager)
        observeLoginStatus(
            profileModel,
            badgeViewModel,
            promotionModel,
            voucherModel,
            onboardingModel,
            benefitModel,
            transactionModel,
            checkoutFlowModel,
            scanningViewModel
        )

    }

    private fun observeSessionExpiry(model: OnboardingScreenViewModel, forceAuthManager: ForceAuthManager) {
        forceAuthManager.authenticationStatusLiveData.observe(this) { status ->
            if (ForceAuthManager.AuthenticationStatus.UNAUTHENTICATED == status) {
                Logger.d(TAG, "observeSessionExpiry() status: $status")
                model.logoutAndClearAllSettingsAfterSessionExpiry(applicationContext)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun observeLoginStatus(profileModel: MembershipProfileViewModelInterface,
                                   badgeViewModel: BadgeViewModelInterface,
                                   promotionModel: MyPromotionViewModelInterface,
                                   voucherModel: VoucherViewModelInterface,
                                   onboardingModel: OnBoardingViewModelAbstractInterface,
                                   benefitModel: BenefitViewModelInterface,
                                   transactionModel: TransactionViewModelInterface,
                                   checkoutFlowModel: CheckOutFlowViewModelInterface,
                                   scanningViewModel: ScanningViewModelInterface
    ) {
        onboardingModel.logoutStateLiveData.observe(this) { logoutState ->
            run {
                if (LogoutState.LOGOUT_SUCCESS_AFTER_SESSION_EXPIRY == logoutState) {
                    Logger.d(TAG, "observeLoginStatus() logout success")
                    setContent {
                        rememberNavController().clearBackStack(0)
                        MainScreenStart(
                            profileModel,
                            badgeViewModel,
                            promotionModel,
                            voucherModel,
                            onboardingModel,
                            benefitModel,
                            transactionModel,
                            checkoutFlowModel,
                            scanningViewModel
                        )
                    }
                }
            }
        }
    }
}

