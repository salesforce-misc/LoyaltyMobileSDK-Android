package com.salesforce.loyalty.mobile.myntorewards.views


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.LogoutState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OnboardingScreenViewModel
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get

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
                HomeTabScreen()
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
                Log.d(TAG, "observeSessionExpiry() status: $status")
                model.logoutAndClearAllSettings(applicationContext)
            }
        }
    }

    private fun observeLoginStatus(model: OnboardingScreenViewModel) {
        model.logoutStateLiveData.observe(this) { logoutState ->
            run {
                if (LogoutState.LOGOUT_SUCCESS == logoutState) {
                    Log.d(TAG, "observeLoginStatus() logout success")
                    setContent {
                        rememberNavController().clearBackStack(0)
                        MainScreenStart()
                    }
                }
            }
        }
    }
}

