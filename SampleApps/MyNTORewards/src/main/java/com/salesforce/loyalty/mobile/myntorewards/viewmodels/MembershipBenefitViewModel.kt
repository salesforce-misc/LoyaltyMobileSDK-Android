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
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BenefitViewStates
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefitsResponse
import kotlinx.coroutines.launch

class MembershipBenefitViewModel : ViewModel() {

    private val TAG = MembershipBenefitViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    //live data for login status
    val membershipBenefitLiveData: LiveData<List<MemberBenefit>>
        get() = membershipBenefit

    private val membershipBenefit = MutableLiveData<List<MemberBenefit>>()

    val benefitViewState: LiveData<BenefitViewStates>
        get() = viewState

    private val viewState = MutableLiveData<BenefitViewStates>()

    fun loadBenefits(context: Context) {
        viewState.postValue(BenefitViewStates.BenefitFetchInProgress)

        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context)
                    .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Log.d(TAG, "failed: member benefit Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val memberId = member.loyaltyProgramMemberId ?: ""
            val membershipKey = member.membershipNumber ?: ""
            val benefitsCache = LocalFileManager.getData(
                context,
                membershipKey,
                LocalFileManager.DIRECTORY_BENEFITS,
                MemberBenefitsResponse::class.java
            )

            Log.d(TAG, "cache : $benefitsCache")
            if (benefitsCache == null) {
                memberBenefitAPI(context)
            } else {
                membershipBenefit.value = benefitsCache.memberBenefits
                viewState.postValue(BenefitViewStates.BenefitFetchSuccess)
            }
        }
    }

    internal fun memberBenefitAPI(context: Context) {
        viewState.postValue(BenefitViewStates.BenefitFetchInProgress)
        val memberJson =
            PrefHelper.customPrefs(context)
                .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
        if (memberJson == null) {
            Log.d(TAG, "failed: member benefit Member details not present")
            return
        }
        val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
        val memberId = member.loyaltyProgramMemberId ?: ""
        val membershipKey = member.membershipNumber ?: ""

        viewModelScope.launch {
            loyaltyAPIManager.getMemberBenefits(memberId, membershipKey).onSuccess {

                membershipBenefit.value = it.memberBenefits

                LocalFileManager.saveData(
                    context,
                    it,
                    membershipKey,
                    LocalFileManager.DIRECTORY_BENEFITS
                )
                viewState.postValue(BenefitViewStates.BenefitFetchSuccess)
                Log.d(TAG, "success member benefit response: $it")

            }.onFailure {
                Log.d(TAG, "failed: member benefit ${it.message}")
                viewState.postValue(BenefitViewStates.BenefitFetchFailure)

            }
        }
    }
}
