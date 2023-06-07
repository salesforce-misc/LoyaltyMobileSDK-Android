package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager


//view model
class OnboardingScreenViewModel2 : OnBoardingViewModelAbstractInterface {
    private val TAG = OnboardingScreenViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager? = null
    //live data for login status
    override val loginStatusLiveData: LiveData<LoginState>
        get() = loginStatus

    private val loginStatus = MutableLiveData<LoginState>()

    //live data for join status
    override val enrollmentStatusLiveData: LiveData<EnrollmentState>
        get() = enrollmentStatus

    private val enrollmentStatus = MutableLiveData<EnrollmentState>()

    //live data for logout state
    override val logoutStateLiveData: LiveData<LogoutState>
        get() = logoutState

    private val logoutState = MutableLiveData<LogoutState>()

    //Setting up enrollment status as default after enrollment result. this is to avoid duplicate observation when compose recreate
    override fun resetEnrollmentStatusDefault() {
        enrollmentStatus.value = EnrollmentState.ENROLLMENT_DEFAULT_EMPTY
    }
    //Setting up login status as default after enrollment result. this is to avoid duplicate observation when compose recreate

    override fun resetLoginStatusDefault() {
        loginStatus.value = LoginState.LOGIN_DEFAULT_EMPTY
    }

    //invoke Login API. Since login Mechanism yet to be in place API fetching token and giving pass or fail. That token result
    //is being considered for login result as of now but it will be changed. Token will move to SDK code and will be replaced by Login API
    override fun loginUser(emailAddressText: String, passwordText: String, context: Context) {
        Log.d(TAG, "akash_test" + emailAddressText + "password: " + passwordText)
        loginStatus.value = LoginState.LOGIN_IN_PROGRESS
        loginStatus.value = LoginState.LOGIN_SUCCESS
        }

    //invoke enrollment API
    /*
    * Upon Success or Failure of Enrollment API Enrollment status will be updated
    * Status is being updated in the form of Live data which is being observed inside EnrollementUI
    * Based om status change State change will trigger and corresponding flow will be executed
    * */
    override fun enrollUser(
        firstNameText: String,
        lastNameText: String,
        mobileNumberText: String,
        emailAddressText: String,
        passwordText: String,
        confirmPasswordText: String,
        mailCheckedState: Boolean,
        tncCheckedState: Boolean,
        context: Context
    ) {
        EnrollmentState.ENROLLMENT_SUCCESS
    }

    override fun logoutAndClearAllSettings(context: Context) {
        logoutState.value = LogoutState.LOGOUT_IN_PROGRESS
        logoutState.postValue(LogoutState.LOGOUT_SUCCESS)
    }
}