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
import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentResponse
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


//invoke token API
    fun invokeTokenGenerationApi(emailAddressText:String, passwordText:String) {

    Log.d("OnboardingScreenViewModel","email: "+ emailAddressText+ "password: "+passwordText )
        viewModelScope.launch {
            var accessTokenResponse: String? = null
            ForceAuthManager.getAccessToken().onSuccess {
                accessTokenResponse = it.accessToken

            }
                .onFailure {
                    Log.d("OnboardingScreenViewModel", "Access token request failed: ${it.message}")
                    loginStatus.value= "Login Failure"
                }
            if (accessTokenResponse != null) {
                loginStatus.value= "Login Success"
                Log.d("OnboardingScreenViewModel", "Access token Success: $accessTokenResponse")
            }
        }
    }


//invoke enrollment API
     fun invokeEnrollmentApi(firstNameText:String, lastNameText:String, mobileNumberText:String, emailAddressText:String, passwordText:String, confirmPasswordText:String) {

    Log.d("OnboardingScreenViewModel","name is"+ firstNameText)
    viewModelScope.launch {
            var enrollmentResponse: EnrollmentResponse? = null
            LoyaltyAPIManager.postEnrollment(
                firstNameText,
                lastNameText,
                emailAddressText,
                mobileNumberText,
                true
            ).onSuccess {
                enrollmentResponse = it
            }
                .onFailure {
                    enrollmentStatus.value= "Enrollment Failure"
                    Log.d("OnboardingScreenViewModel", "Enrollment request failed: ${it.message}")
                }
            if (enrollmentResponse != null) {
                enrollmentStatus.value= "Enrollment Success"
                Log.d("OnboardingScreenViewModel", "Enrollment request Success: $enrollmentResponse")
            }
        }
    }
}