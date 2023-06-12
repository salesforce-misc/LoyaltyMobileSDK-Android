package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MyPromotionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import kotlinx.coroutines.launch

class MyPromotionViewModel : ViewModel(), MyPromotionViewModelInterface {

    private val TAG = MyPromotionViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    override val membershipPromotionLiveData: LiveData<PromotionsResponse>
        get() = membershipPromo

    private val membershipPromo = MutableLiveData<PromotionsResponse>()

    override val promEnrollmentStatusLiveData: LiveData<PromotionEnrollmentUpdateState>
        get() = promEnrollmentStatus

    private val promEnrollmentStatus = MutableLiveData<PromotionEnrollmentUpdateState>()

    override val promotionViewState: LiveData<PromotionViewState>
        get() = viewState

    private val viewState = MutableLiveData<PromotionViewState>()

    //Setting up enrollment status as default after enrollment result. this is to avoid duplicate observation when compose recreate
    override fun resetPromEnrollmentStatusDefault() {
        promEnrollmentStatus.value =
            PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_DEFAULT_EMPTY
    }

    override fun loadPromotions(context: Context, refreshRequired:Boolean) {
        viewState.postValue(PromotionViewState.PromotionFetchInProgress)
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Log.d(TAG, "failed: member promotion Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val memberId = member.loyaltyProgramMemberId ?: ""
            val membershipKey = member.membershipNumber ?: ""
            if(refreshRequired)
            {
                fetchPromotions(context, memberId, membershipKey)
            }
            else{
                val promotionCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_PROMOTIONS,
                    PromotionsResponse::class.java
                )

                Log.d(TAG, "cache : $promotionCache")
                if (promotionCache == null) {
                    fetchPromotions(context, memberId, membershipKey)
                } else {
                    membershipPromo.value= promotionCache!!
                    viewState.postValue(PromotionViewState.PromotionsFetchSuccess)
                }
            }


        }
    }

    private fun fetchPromotions(context: Context, memberId: String, membershipKey: String) {
        viewModelScope.launch {
            loyaltyAPIManager.getEligiblePromotions(membershipKey, memberId).onSuccess {
                if (it.outputParameters?.outputParameters?.results != null) {
                    LocalFileManager.saveData(
                        context,
                        it,
                        membershipKey,
                        LocalFileManager.DIRECTORY_PROMOTIONS
                    )
                    membershipPromo.value= it
                    viewState.postValue(PromotionViewState.PromotionsFetchSuccess)
                } else {
                    viewState.postValue(PromotionViewState.PromotionsFetchFailure)
                }
                Log.d(TAG, "success member promotion response: $it")
            }.onFailure {

                viewState.postValue(PromotionViewState.PromotionsFetchFailure)
                Log.d(TAG, "failed: member promotion ${it.message}")
            }
        }
    }

    override fun enrollInPromotions(context: Context, promotionName: String) {
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Log.d(TAG, "failed: member promotion Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            var membershipNumber = member.membershipNumber ?: ""

            loyaltyAPIManager.enrollInPromotions(membershipNumber, promotionName).onSuccess {
                promEnrollmentStatus.value =
                    PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_SUCCESS
                loadPromotions(context, true)
                Log.d(TAG, "promotion enrolled success: $it")
            }.onFailure {
                promEnrollmentStatus.value =
                    PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_FAILURE
                Log.d(TAG, "promotion enrolled failed ${it.message}")
            }
        }
    }

    override fun unEnrollInPromotions(context: Context, promotionName: String) {
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Log.d(TAG, "failed: member promotion Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            var membershipNumber = member.membershipNumber ?: ""
            loyaltyAPIManager.unEnrollPromotion(membershipNumber, promotionName).onSuccess {
                promEnrollmentStatus.value =
                    PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_SUCCESS
                loadPromotions(context, true)
                Log.d(TAG, "promotion un enrolled success: $it")
            }.onFailure {
                promEnrollmentStatus.value =
                    PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_FAILURE
                Log.d(TAG, "promotion un enrolled failed ${it.message}")
            }
        }
    }
}