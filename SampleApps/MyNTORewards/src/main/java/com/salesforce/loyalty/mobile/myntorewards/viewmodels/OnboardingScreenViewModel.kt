package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_COMMUNITY_MEMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_LOGIN_SUCCESSFUL
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import kotlinx.coroutines.launch


//view model
open class OnboardingScreenViewModel : ViewModel(), OnBoardingViewModelAbstractInterface {
    private val TAG = OnboardingScreenViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
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
        Logger.d(TAG, "email: " + emailAddressText + "password: " + passwordText)
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
                Logger.d(TAG, "Access token Success: $accessTokenResponse")
                val memberProfileResponse = loyaltyAPIManager.getMemberProfile(null, null, null)
                memberProfileResponse.onSuccess {
                    if (it != null) {
                        val memberId = it.loyaltyProgramMemberId
                        val memberShipNumber = it.membershipNumber
                        val communityMemberModel = CommunityMemberModel(
                            firstName = it.associatedContact?.firstName,
                            lastName = it.associatedContact?.lastName,
                            email = it.associatedContact?.email,
                            loyaltyProgramMemberId = it.loyaltyProgramMemberId,
                            loyaltyProgramName = it.loyaltyProgramName,
                            membershipNumber = it.membershipNumber
                        )
                        memberId?.let { loyaltyMemberId ->
                            val member = Gson().toJson(communityMemberModel, CommunityMemberModel::class.java)
                            PrefHelper.customPrefs(context).set(KEY_COMMUNITY_MEMBER, member)
                        }

                        if (memberShipNumber != null) {
                            LocalFileManager.saveData(
                                context,
                                it,
                                memberShipNumber,
                                LocalFileManager.DIRECTORY_PROFILE
                            )
                        }
                        // Set Login status to success
                        PrefHelper.customPrefs(context)
                            .set(KEY_LOGIN_SUCCESSFUL, true)
                        loginStatus.value = LoginState.LOGIN_SUCCESS
                    } else {
                        loginStatus.value = LoginState.LOGIN_SUCCESS_ENROLLMENT_REQUIRED
                    }
                }.onFailure {
                    loginStatus.value = LoginState.LOGIN_SUCCESS_ENROLLMENT_REQUIRED
                }

            } else {
                loginStatus.value = LoginState.LOGIN_FAILURE
                Logger.d(TAG, "Access token Failure")
            }
        }
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
        enrollmentStatus.value =
            EnrollmentState.ENROLLMENT_SUCCESS
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
                val communityMemberModel = CommunityMemberModel(
                    firstName = firstNameText,
                    lastName = lastNameText,
                    email = emailAddressText,
                    loyaltyProgramMemberId = it.loyaltyProgramMemberId,
                    loyaltyProgramName = it.loyaltyProgramName,
                    membershipNumber = it.membershipNumber
                )
                val member = Gson().toJson(communityMemberModel, CommunityMemberModel::class.java)
                PrefHelper.customPrefs(context).set(KEY_COMMUNITY_MEMBER, member)
            }
                .onFailure {
                    enrollmentStatus.value =
                        EnrollmentState.ENROLLMENT_FAILURE   // enrollment state is being observed in Enrollment UI Composable
                    Logger.d(TAG, "Enrollment request failed: ${it.message}")
                }
            if (enrollmentResponse != null) {
                enrollmentStatus.value =
                    EnrollmentState.ENROLLMENT_SUCCESS   // enrollment state is being observed in Enrollment UI Composable
                Logger.d(TAG, "Enrollment request Success: $enrollmentResponse")
            }
        }
    }

    override fun logoutAndClearAllSettings(context: Context) {
        logoutState.value = LogoutState.LOGOUT_IN_PROGRESS
        viewModelScope.launch {
            ForceAuthManager.forceAuthManager.revokeAccessToken()
            // clear Shared Preferences
            ForceAuthEncryptedPreference.clearAll(context)
            ForceConnectedAppEncryptedPreference.clearAll(context)
            PrefHelper.clearAll(context)

            //clear cache
            LocalFileManager.clearAllFolders(context)
            logoutState.postValue(LogoutState.LOGOUT_SUCCESS)
        }
    }
}