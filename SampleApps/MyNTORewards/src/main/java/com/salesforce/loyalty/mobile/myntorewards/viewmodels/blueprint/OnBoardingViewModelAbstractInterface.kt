package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.EnrollmentState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.LoginState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.LogoutState

interface OnBoardingViewModelAbstractInterface {
    val loginStatusLiveData: LiveData<LoginState>
    val enrollmentStatusLiveData: LiveData<EnrollmentState>
    val logoutStateLiveData: LiveData<LogoutState>
    fun resetEnrollmentStatusDefault()
    fun resetLoginStatusDefault()
    fun loginUser(emailAddressText: String, passwordText: String, context: Context)
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
    )
    fun logoutAndClearAllSettings(context: Context)

}