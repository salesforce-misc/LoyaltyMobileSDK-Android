package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BenefitViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BenefitViewStates
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefitsResponse
import kotlinx.coroutines.launch

class MembershipBenefitViewModel(private val loyaltyAPIManager: LoyaltyAPIManager) : ViewModel(), BenefitViewModelInterface {

    private val TAG = MembershipBenefitViewModel::class.java.simpleName

    //live data for login status
    override val membershipBenefitLiveData: LiveData<List<MemberBenefit>>
        get() = membershipBenefit

    private val membershipBenefit = MutableLiveData<List<MemberBenefit>>()

    override val benefitViewState: LiveData<BenefitViewStates>
        get() = viewState

    private val viewState = MutableLiveData<BenefitViewStates>()

    override fun loadBenefits(context: Context, refreshRequired:Boolean) {


        viewModelScope.launch {
            viewState.postValue(BenefitViewStates.BenefitFetchInProgress)
            val memberJson =
                PrefHelper.customPrefs(context)
                    .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Logger.d(TAG, "failed: member benefit Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val memberId = member.loyaltyProgramMemberId ?: ""
            val membershipKey = member.membershipNumber ?: ""

            if(refreshRequired)
            {
                memberBenefitAPI(context, memberId,membershipKey )
            }
            else{
                val benefitsCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_BENEFITS,
                    MemberBenefitsResponse::class.java
                )

                Logger.d(TAG, "cache : $benefitsCache")
                if (benefitsCache == null || refreshRequired) {
                    memberBenefitAPI(context, memberId, membershipKey)
                } else {
                    membershipBenefit.value = benefitsCache.memberBenefits
                    viewState.postValue(BenefitViewStates.BenefitFetchSuccess)
                }
            }

        }
    }

    private fun memberBenefitAPI(context: Context, memberId: String, membershipKey: String) {
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
                Logger.d(TAG, "success member benefit response: $it")

            }.onFailure {
                Logger.d(TAG, "failed: member benefit ${it.message}")
                viewState.postValue(BenefitViewStates.BenefitFetchFailure)

            }
        }
    }
}
