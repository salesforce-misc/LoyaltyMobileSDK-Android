package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthEncryptedPreference
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceConfig
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceConnectedAppEncryptedPreference
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_COMMUNITY_MEMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_LOGIN_SUCCESSFUL
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.EnrollmentChannel
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberStatus
import com.salesforce.loyalty.mobile.sources.loyaltyModels.TransactionalJournalStatementFrequency
import com.salesforce.loyalty.mobile.sources.loyaltyModels.TransactionalJournalStatementMethod
import kotlinx.coroutines.launch


//view model
open class OnboardingScreenViewModel(
    private val loyaltyAPIManager: LoyaltyAPIManager,
    val forceAuthManager: ForceAuthManager
) : ViewModel(), OnBoardingViewModelAbstractInterface {
    private val TAG = OnboardingScreenViewModel::class.java.simpleName

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


    override fun resetLogOutDefault() {
        logoutState.value = LogoutState.LOGOUT_DEFAULT_EMPTY
    }

    //invoke Login API. Since login Mechanism yet to be in place API fetching token and giving pass or fail. That token result
    //is being considered for login result as of now but it will be changed. Token will move to SDK code and will be replaced by Login API
    override fun loginUser(emailAddressText: String, passwordText: String, context: Context) {
        Logger.d(TAG, "email: " + emailAddressText + "password: " + passwordText)
        loginStatus.value = LoginState.LOGIN_IN_PROGRESS
        viewModelScope.launch {
            val connectedApp = forceAuthManager.getConnectedApp()
            var accessTokenResponse: String? = null
            accessTokenResponse = forceAuthManager.authenticate(
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
                        val memberId = it.loyaltyProgramMemberId
                        val memberShipNumber = it.membershipNumber
                        val communityMemberModel = CommunityMemberModel(
                            firstName = it.associatedContact?.firstName,
                            lastName = it.associatedContact?.lastName,
                            email = it.associatedContact?.email,
                            loyaltyProgramMemberId = it.loyaltyProgramMemberId,
                            loyaltyProgramName = it.loyaltyProgramName,
                            membershipNumber = it.membershipNumber,
                            contactId = it.associatedContact?.contactId
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
                }.onFailure {
                    val errorMessage = it.localizedMessage
                    Logger.d(TAG, "Member Profile failure: ${errorMessage}")
                    if (errorMessage.contains(ForceConfig.HTTP_RESPONSE_CODE_SERVER_ERROR.toString())) {
                        loginStatus.value = LoginState.LOGIN_SUCCESS_ENROLLMENT_REQUIRED
                    } else {
                        loginStatus.value = LoginState.LOGIN_FAILURE
                    }
                }

            } else {
                loginStatus.value = LoginState.LOGIN_FAILURE
                Logger.d(TAG, "Access token Failure")
            }
        }
    }


    override fun logoutAndClearAllSettings(context: Context) {
        logoutState.value = LogoutState.LOGOUT_IN_PROGRESS
        viewModelScope.launch {
            forceAuthManager.revokeAccessToken()
            // clear Shared Preferences
            ForceAuthEncryptedPreference.clearAll(context)
            PrefHelper.clearAll(context)
            ReferralsLocalRepository.clearReferralsData()

            //clear cache
            LocalFileManager.clearAllFolders(context)
            logoutState.postValue(LogoutState.LOGOUT_SUCCESS)
        }
    }

    override fun logoutAndClearAllSettingsAfterSessionExpiry(context: Context) {
        logoutState.value = LogoutState.LOGOUT_IN_PROGRESS
        viewModelScope.launch {
            forceAuthManager.revokeAccessToken()
            // clear Shared Preferences
            ForceAuthEncryptedPreference.clearAll(context)
            PrefHelper.clearAll(context)
            ReferralsLocalRepository.clearReferralsData()

            //clear cache
            LocalFileManager.clearAllFolders(context)
            logoutState.postValue(LogoutState.LOGOUT_SUCCESS_AFTER_SESSION_EXPIRY)
        }
    }

    override fun joinUser(email: String, context: Context) {
        Logger.d(TAG, "joinUser()")
        enrollmentStatus.value = EnrollmentState.ENROLLMENT_IN_PROGRESS
        viewModelScope.launch {
            val contactRecord =
                forceAuthManager.getFirstNameLastNameFromContactEmail(email)
            contactRecord?.let {
                val firstName = it.firstName ?:""
                val lastName = it.lastName ?: ""
                val phone = it.phone
                val additionalContactAttributes = mapOf(AppConstants.KEY_PHONE to phone)
                loyaltyAPIManager.postEnrollment(
                    firstName,
                    lastName,
                    email,
                    additionalContactAttributes,
                    true,
                    MemberStatus.ACTIVE,
                    true,
                    TransactionalJournalStatementFrequency.MONTHLY,
                    TransactionalJournalStatementMethod.EMAIL,
                    EnrollmentChannel.EMAIL,
                    true,
                    true,
                ).onSuccess {

                    val memberProfileResponse = loyaltyAPIManager.getMemberProfile(null, null, null)
                    memberProfileResponse.onSuccess {
                        val memberId = it.loyaltyProgramMemberId
                        val memberShipNumber = it.membershipNumber
                        val communityMemberModel = CommunityMemberModel(
                            firstName = it.associatedContact?.firstName,
                            lastName = it.associatedContact?.lastName,
                            email = it.associatedContact?.email,
                            loyaltyProgramMemberId = it.loyaltyProgramMemberId,
                            loyaltyProgramName = it.loyaltyProgramName,
                            membershipNumber = it.membershipNumber,
                            contactId = it.associatedContact?.contactId
                        )
                        memberId?.let { loyaltyMemberId ->
                            val member = Gson().toJson(
                                communityMemberModel, CommunityMemberModel::class.java
                            )
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
                        PrefHelper.customPrefs(context).set(KEY_LOGIN_SUCCESSFUL, true)
                        enrollmentStatus.value = EnrollmentState.ENROLLMENT_SUCCESS
                    }.onFailure {
                        enrollmentStatus.value = EnrollmentState.ENROLLMENT_FAILURE
                    }
                }.onFailure {
                        enrollmentStatus.value =
                            EnrollmentState.ENROLLMENT_FAILURE   // enrollment state is being observed in Enrollment UI Composable
                        Logger.d(TAG, "Enrollment request failed: ${it.message}")
                    }
            }
        }
    }

    override fun getSelfRegisterUrl(): String {
        return forceAuthManager.getConnectedApp().selfRegisterUrl
    }

    override fun getSelfRegisterRedirectUrl(): String {
        return forceAuthManager.getConnectedApp().communityUrl + AppConstants.SELF_REGISTER_REDIRECT_URL_PATH
    }
}