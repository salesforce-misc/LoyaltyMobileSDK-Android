package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_MEMBERSHIP_NUMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_PROGRAM_MEMBER_ID
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberProfileResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results
import kotlinx.coroutines.launch

class MembershipProfileViewModel : ViewModel() {
    private val TAG = MembershipProfileViewModel::class.java.simpleName

    //live data for login status
    val membershipProfileLiveData: LiveData<MemberProfileResponse>
        get() = membershipProfile

    private val membershipProfile = MutableLiveData<MemberProfileResponse>()

    fun getMemberProfile(context: Context) {
        viewModelScope.launch {
            var memberID = PrefHelper.customPrefs(context)[KEY_PROGRAM_MEMBER_ID, ""]
            var membershipKey = PrefHelper.customPrefs(context)[KEY_MEMBERSHIP_NUMBER, ""]
            LoyaltyAPIManager.getMemberProfile(memberID, membershipKey, null).onSuccess {
                membershipProfile.value = it
                Log.d(TAG, "member success: $it")
            }.onFailure {
                Log.d(TAG, "member failed: ${it.message}")
            }
        }
    }

}