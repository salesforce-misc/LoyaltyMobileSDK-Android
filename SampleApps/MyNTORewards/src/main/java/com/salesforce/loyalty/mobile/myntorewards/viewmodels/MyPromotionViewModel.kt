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
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import kotlinx.coroutines.launch

class MyPromotionViewModel : ViewModel() {

    private val TAG = MyPromotionViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    val membershipPromotionLiveData: LiveData<List<Results>>
        get() = membershipPromo

    private val membershipPromo = MutableLiveData<List<Results>>()

    val promEnrollmentStatusLiveData: LiveData<PromotionEnrollmentUpdateState>
        get() = promEnrollmentStatus

    private val promEnrollmentStatus = MutableLiveData<PromotionEnrollmentUpdateState>()

    val promotionViewState: LiveData<PromotionViewState>
        get() = viewState

    private val viewState = MutableLiveData<PromotionViewState>()

    //Setting up enrollment status as default after enrollment result. this is to avoid duplicate observation when compose recreate
    fun resetPromEnrollmentStatusDefault() {
        promEnrollmentStatus.value =
            PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_DEFAULT_EMPTY
    }

    fun loadPromotions(context: Context) {
        viewState.postValue(PromotionViewState.PromotionFetchInProgress)
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Log.d(TAG, "failed: member promotion Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)

            val memberId =
                member.loyaltyProgramMemberId
            var membershipKey = member.membershipNumber ?: ""

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
                viewState.postValue(PromotionViewState.PromotionsFetchSuccess(promotionCache))
            }
        }
    }

    private fun fetchPromotions(context: Context, memberId: String?, membershipNumber: String) {
        viewModelScope.launch {

            loyaltyAPIManager.getEligiblePromotions(membershipNumber, memberId).onSuccess {
                if (it.outputParameters?.outputParameters?.results != null) {
                    LocalFileManager.saveData(
                        context,
                        it,
                        membershipNumber,
                        LocalFileManager.DIRECTORY_PROMOTIONS
                    )
                    viewState.postValue(PromotionViewState.PromotionsFetchSuccess(it))
                } else {
                    viewState.postValue(PromotionViewState.PromotionsFetchFailure(it.message))
                }
                Log.d(TAG, "success member promotion response: $it")
            }.onFailure {
                viewState.postValue(PromotionViewState.PromotionsFetchFailure(it.message))
                Log.d(TAG, "failed: member promotion ${it.message}")
            }
        }
    }

    fun enrollInPromotions(context: Context, promotionName: String) {
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
                loadPromotions(context)
                Log.d(TAG, "promotion enrolled success: $it")
            }.onFailure {
                promEnrollmentStatus.value =
                    PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_FAILURE
                Log.d(TAG, "promotion enrolled failed ${it.message}")
            }
        }
    }

    fun unEnrollInPromotions(context: Context, promotionName: String) {
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
                loadPromotions(context)
                Log.d(TAG, "promotion un enrolled success: $it")
            }.onFailure {
                promEnrollmentStatus.value =
                    PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_FAILURE
                Log.d(TAG, "promotion un enrolled failed ${it.message}")
            }
        }
    }
}