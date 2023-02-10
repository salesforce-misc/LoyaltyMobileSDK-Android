package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.util.Log
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import kotlinx.coroutines.launch

//view model
class OnboardingScreenViewModel : ViewModel() {

    //live data for login status
    val loginStatusLiveData: LiveData<String>
        get() = loginStatus

    private val loginStatus = MutableLiveData<String>()

    //live data for join status
    val enrollmentStatusLiveData: LiveData<String>
        get() = enrollmentStatus

    private val enrollmentStatus = MutableLiveData<String>()

    //Setting up enrollment status as default after enrollment result. this is to avoid duplicate observation when compose recreate
    fun resetEnrollmentStatusDefault() {
        enrollmentStatus.value = "Enrollment Status Empty"
    }
    //Setting up login status as default after enrollment result. this is to avoid duplicate observation when compose recreate

    fun resetLoginStatusDefault() {
        loginStatus.value = "Login Status Empty"
    }

    //invoke Login API. Since login Mechanism yet to be in place API fetching token and giving pass or fail. That token result
    //is being considered for login result as of now but it will be changed. Token will move to SDK code and will be replaced by Login API
    fun loginUser(emailAddressText: String, passwordText: String) {

        Log.d(
            "OnboardingScreenViewModel",
            "email: " + emailAddressText + "password: " + passwordText
        )
        viewModelScope.launch {
            var accessTokenResponse: String? = null
            ForceAuthManager.getAccessToken().onSuccess {
                accessTokenResponse = it.accessToken

            }
                .onFailure {
                    Log.d("OnboardingScreenViewModel", "Access token request failed: ${it.message}")
                    loginStatus.value = "Login Failure"
                }
            if (accessTokenResponse != null) {
                loginStatus.value = "Login Success"
                Log.d("OnboardingScreenViewModel", "Access token Success: $accessTokenResponse")
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
        confirmPasswordText: String
    ) {

        viewModelScope.launch {
            var enrollmentResponse: EnrollmentResponse? = null
            LoyaltyAPIManager.postEnrollment(
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
                true
                ).onSuccess {
                enrollmentResponse = it
            }
                .onFailure {
                    enrollmentStatus.value =
                        "Enrollment Failure"    // enrollment state is being observed in Enrollment UI Composable
                    Log.d("OnboardingScreenViewModel", "Enrollment request failed: ${it.message}")
                }
            if (enrollmentResponse != null) {
                enrollmentStatus.value =
                    "Enrollment Success"   // enrollment state is being observed in Enrollment UI Composable
                Log.d(
                    "OnboardingScreenViewModel",
                    "Enrollment request Success: $enrollmentResponse"
                )
            }
        }
    }
}