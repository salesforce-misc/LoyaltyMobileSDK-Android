package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyProfileViewStates
import com.salesforce.loyalty.mobile.sources.PrefHelper
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
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Log.d(TAG, "failed: member profile Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)

            val memberId =
                member.loyaltyProgramMemberId
            var membershipKey = member.membershipNumber ?: ""

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

    fun getMemberProfile(context: Context) {
        viewState.postValue(MyProfileViewStates.MyProfileFetchInProgress)
        val memberJson =
            PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
        if (memberJson == null) {
            Log.d(TAG, "failed: member profile Member details not present")
            return
        }
        val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)

        val memberId = member.loyaltyProgramMemberId

        var membershipKey = member.membershipNumber ?: ""

        viewModelScope.launch {
            loyaltyAPIManager.getMemberProfile(memberId, membershipKey, null).onSuccess {

                if (it.membershipNumber != null) {
                    LocalFileManager.saveData(
                        context,
                        it,
                        it.membershipNumber!!,
                        LocalFileManager.DIRECTORY_PROFILE
                    )
                }

                membershipProfile.value = it
                val communityMemberModel = CommunityMemberModel(
                    firstName = it.associatedContact?.firstName,
                    lastName = it.associatedContact?.lastName,
                    email = it.associatedContact?.email,
                    loyaltyProgramMemberId = it.loyaltyProgramMemberId,
                    loyaltyProgramName = it.loyaltyProgramName,
                    membershipNumber = it.membershipNumber
                )
                memberId?.let { loyaltyMemberId ->
                    val member =
                        Gson().toJson(communityMemberModel, CommunityMemberModel::class.java)
                    PrefHelper.customPrefs(context).set(AppConstants.KEY_COMMUNITY_MEMBER, member)
                }

                viewState.postValue(MyProfileViewStates.MyProfileFetchSuccess)
                Log.d(TAG, "member success: $it")
            }.onFailure {
                Log.d(TAG, "member failed: ${it.message}")
                viewState.postValue(MyProfileViewStates.MyProfileFetchFailure)
            }
        }
    }

}

