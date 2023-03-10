package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import kotlinx.coroutines.launch

class MyPromotionViewModel : ViewModel() {

    private val TAG = MyPromotionViewModel::class.java.simpleName

    val membershipPromotionLiveData: LiveData<List<Results>>
        get() = membershipPromo

    private val membershipPromo = MutableLiveData<List<Results>>()

    fun promotionAPI(context: Context) {
        viewModelScope.launch {
            val membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""
            val memberID =
                PrefHelper.customPrefs(context)[AppConstants.KEY_PROGRAM_MEMBER_ID, ""] ?: ""
            LoyaltyAPIManager.getEligiblePromotions("M0001", memberID).onSuccess {
                membershipPromo.value = it.outputParameters?.outputParameters?.results
                Log.d(TAG, "success member promotion response: $it")
            }.onFailure {
                Log.d(TAG, "failed: member promotion ${it.message}")
            }
        }
    }

}