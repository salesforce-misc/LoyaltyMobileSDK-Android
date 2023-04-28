package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_EMAIL_ID
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_MEMBERSHIP_NUMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_PROGRAM_MEMBER_ID
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_PROGRAM_NAME
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import kotlinx.coroutines.launch


//view model
class OnboardingScreenViewModel : ViewModel() {
    private val TAG = OnboardingScreenViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    //live data for login status
    val loginStatusLiveData: LiveData<LoginState>
        get() = loginStatus

    private val loginStatus = MutableLiveData<LoginState>()

    //live data for join status
    val enrollmentStatusLiveData: LiveData<EnrollmentState>
        get() = enrollmentStatus

    private val enrollmentStatus = MutableLiveData<EnrollmentState>()

    //Setting up enrollment status as default after enrollment result. this is to avoid duplicate observation when compose recreate
    fun resetEnrollmentStatusDefault() {
        enrollmentStatus.value = EnrollmentState.ENROLLMENT_DEFAULT_EMPTY
    }
    //Setting up login status as default after enrollment result. this is to avoid duplicate observation when compose recreate

    fun resetLoginStatusDefault() {
        loginStatus.value = LoginState.LOGIN_DEFAULT_EMPTY
    }

    //invoke Login API. Since login Mechanism yet to be in place API fetching token and giving pass or fail. That token result
    //is being considered for login result as of now but it will be changed. Token will move to SDK code and will be replaced by Login API
    fun loginUser(emailAddressText: String, passwordText: String, context: Context) {
        Log.d(TAG, "email: " + emailAddressText + "password: " + passwordText)
        loginStatus.value = LoginState.LOGIN_IN_PROGRESS
        viewModelScope.launch {
            val connectedApp = ForceAuthManager.forceAuthManager.getConnectedApp()
            var accessTokenResponse: String? = null
            accessTokenResponse = ForceAuthManager.forceAuthManager.authenticate(
                communityUrl = connectedApp.communityUrl,
                consumerKey = connectedApp.consumerKey,
                callbackUrl = connectedApp.callbackUrl,
                userName = emailAddressText,
                password = passwordText
            )

            if (accessTokenResponse != null) {
                Log.d(TAG, "Access token Success: $accessTokenResponse")
                val memberProfileResponse = loyaltyAPIManager.getMemberProfile(null, null, null)
                memberProfileResponse.onSuccess {
                    if (it != null) {
                        val memberId = it.loyaltyProgramMemberId
                        memberId?.let { loyaltyMemberId ->
                            PrefHelper.customPrefs(context)
                                .set(KEY_PROGRAM_MEMBER_ID, loyaltyMemberId)
                            PrefHelper.customPrefs(context)
                                .set(KEY_MEMBERSHIP_NUMBER, it.membershipNumber)
                        }
                        loginStatus.value = LoginState.LOGIN_SUCCESS
                    } else {
                        loginStatus.value = LoginState.LOGIN_SUCCESS_ENROLLMENT_REQUIRED
                    }
                }.onFailure {
                    loginStatus.value = LoginState.LOGIN_SUCCESS_ENROLLMENT_REQUIRED
                }

            } else {
                loginStatus.value = LoginState.LOGIN_FAILURE
                Log.d(TAG, "Access token Failure")
            }
        }
    }

    //invoke enrollment API
    /*
    * Upon Success or Failure of Enrollment API Enrollment status will be updated
    * Status is being updated in the form of Live data which is being observed inside EnrollementUI
    * Based om status change State change will trigger and corresponding flow will be executed
    * */
    fun enrollUser(
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
        viewModelScope.launch {
            var enrollmentResponse: EnrollmentResponse? = null
            loyaltyAPIManager.postEnrollment(
                firstNameText,
                lastNameText,
                emailAddressText,
                null,
                true,
                MemberStatus.ACTIVE,
                true,
                TransactionalJournalStatementFrequency.MONTHLY,
                TransactionalJournalStatementMethod.EMAIL,
                EnrollmentChannel.EMAIL,
                true,
                true,
            ).onSuccess {
                enrollmentResponse = it
                PrefHelper.customPrefs(context)
                    .set(KEY_MEMBERSHIP_NUMBER, enrollmentResponse!!.membershipNumber)
                PrefHelper.customPrefs(context)
                    .set(KEY_PROGRAM_MEMBER_ID, enrollmentResponse!!.loyaltyProgramMemberId)
                PrefHelper.customPrefs(context)
                    .set(KEY_PROGRAM_NAME, enrollmentResponse!!.loyaltyProgramName)
                PrefHelper.customPrefs(context)
                    .set(KEY_EMAIL_ID, emailAddressText)
            }
                .onFailure {
                    enrollmentStatus.value =
                        EnrollmentState.ENROLLMENT_FAILURE   // enrollment state is being observed in Enrollment UI Composable
                    Log.d(TAG, "Enrollment request failed: ${it.message}")
                }
            if (enrollmentResponse != null) {
                enrollmentStatus.value =
                    EnrollmentState.ENROLLMENT_SUCCESS   // enrollment state is being observed in Enrollment UI Composable
                Log.d(TAG, "Enrollment request Success: $enrollmentResponse")
            }
        }
    }
}