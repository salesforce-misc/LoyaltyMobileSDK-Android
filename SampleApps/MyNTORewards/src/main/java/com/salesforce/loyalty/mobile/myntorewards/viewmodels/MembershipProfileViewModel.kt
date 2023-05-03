package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_FIRSTNAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_LASTNAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_MEMBERSHIP_NUMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_PROGRAM_MEMBER_ID
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyProfileViewStates
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberProfileResponse
import kotlinx.coroutines.launch

class MembershipProfileViewModel : ViewModel() {
    private val TAG = MembershipProfileViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    //live data for login status
    val membershipProfileLiveData: LiveData<MemberProfileResponse?>
        get() = membershipProfile

    private val membershipProfile = MutableLiveData<MemberProfileResponse?>()

    val profileViewState: LiveData<MyProfileViewStates>
        get() = viewState

    private val viewState = MutableLiveData<MyProfileViewStates>()

    fun loadProfile(context: Context) {
        viewState.postValue(MyProfileViewStates.MyProfileFetchInProgress)
        viewModelScope.launch {

            viewModelScope.launch {
                val membershipKey =
                    PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""
                val myProfileCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_PROFILE,
                    MemberProfileResponse::class.java
                )

                Log.d(TAG, "cache : $myProfileCache")
                if (myProfileCache == null) {
                    getMemberProfile(context)
                } else {
                   membershipProfile.value = myProfileCache
                    viewState.postValue(MyProfileViewStates.MyProfileFetchSuccess)
                }
            }
        }
    }


    fun getMemberProfile(context: Context) {
        viewModelScope.launch {
            var memberID = PrefHelper.customPrefs(context)[KEY_PROGRAM_MEMBER_ID, ""]
            var membershipKey = PrefHelper.customPrefs(context)[KEY_MEMBERSHIP_NUMBER, ""]
            loyaltyAPIManager.getMemberProfile(memberID, membershipKey, null).onSuccess {

                if (it.membershipNumber!= null) {
                    LocalFileManager.saveData(
                        context,
                        it,
                        it.membershipNumber!!,
                        LocalFileManager.DIRECTORY_PROFILE
                    )
                }

                membershipProfile.value = it
                PrefHelper.customPrefs(context)
                    .set(KEY_FIRSTNAME, (it.associatedContact?.firstName) ?: "")
                PrefHelper.customPrefs(context)
                    .set(KEY_LASTNAME, (it.associatedContact?.lastName) ?: "")

                viewState.postValue(MyProfileViewStates.MyProfileFetchSuccess)
                Log.d(TAG, "member success: $it")
            }.onFailure {
                Log.d(TAG, "member failed: ${it.message}")
                viewState.postValue(MyProfileViewStates.MyProfileFetchFailure)
            }
        }
    }

}

