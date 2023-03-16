package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import kotlinx.coroutines.launch

class MyPromotionViewModel : ViewModel() {

    private val TAG = MyPromotionViewModel::class.java.simpleName

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
            val membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""
            val cache = LocalFileManager.getData(
                context,
                membershipKey,
                LocalFileManager.DIRECTORY_PROMOTIONS,
                PromotionsResponse::class.java
            )
            Log.d(TAG, "cache : $cache")
            if (cache == null) {
                fetchPromotions(context)
            } else {
                viewState.postValue(PromotionViewState.PromotionsFetchSuccess(cache))
            }
        }
    }

    fun fetchPromotions(context: Context){
        viewModelScope.launch {
            val membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""
            val memberID =
                PrefHelper.customPrefs(context)[AppConstants.KEY_PROGRAM_MEMBER_ID, ""] ?: ""
            LoyaltyAPIManager.getEligiblePromotions(membershipKey, memberID).onSuccess {
                if (it?.outputParameters?.outputParameters?.results != null) {
                    LocalFileManager.saveData(
                        context,
                        it,
                        membershipKey,
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
            val membershipNumber =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""

            LoyaltyAPIManager.enrollInPromotions(membershipNumber, promotionName).onSuccess {
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
            val membershipNumber =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""

            LoyaltyAPIManager.unEnrollPromotion(membershipNumber, promotionName).onSuccess {
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