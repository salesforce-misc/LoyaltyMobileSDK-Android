package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberProfileResponse
import kotlinx.coroutines.launch

class MembershipProfileViewModel : ViewModel() {
    private val TAG = MembershipProfileViewModel::class.java.simpleName

    //live data for login status
    val membershipProfileLiveData: LiveData<MemberProfileResponse>
        get() = membershipProfile

    private val membershipProfile = MutableLiveData<MemberProfileResponse>()
    fun getMemberProfile(context: Context) {
        viewModelScope.launch {
            var memberID = PrefHelper.customPrefs(context)["loyaltyProgramMemberId_key", ""]
            var membershipKey = PrefHelper.customPrefs(context)["membershipNumber_key", ""]
            LoyaltyAPIManager.getMemberProfile(memberID, membershipKey, null).onSuccess {
                membershipProfile.value = it
                Log.d(TAG, "member success: $it")
            }.onFailure {
                Log.d(TAG, "member failed: ${it.message}")
            }
        }
    }

    fun memberBenefitAPI(context: Context) {
        viewModelScope.launch {
            var memberID = PrefHelper.customPrefs(context)["loyaltyProgramMemberId_key", ""] ?: ""
            var membershipKey = PrefHelper.customPrefs(context)["membershipNumber_key", ""] ?: ""
            LoyaltyAPIManager.getMemberBenefits(memberID, membershipKey).onSuccess {
                it
                Log.d(TAG, "success member benefit response: $it")
            }.onFailure {
                Log.d(TAG, "failed: member benefit ${it.message}")
            }
        }
    }
}