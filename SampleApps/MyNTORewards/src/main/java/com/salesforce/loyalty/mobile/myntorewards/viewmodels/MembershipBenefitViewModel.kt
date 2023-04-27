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
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit
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

    fun memberBenefitAPI(context: Context) {
        viewModelScope.launch {
            var memberID =
                PrefHelper.customPrefs(context)[AppConstants.KEY_PROGRAM_MEMBER_ID, ""] ?: ""
            var membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: null
            loyaltyAPIManager.getMemberBenefits(memberID, membershipKey).onSuccess {
                membershipBenefit.value = it.memberBenefits
                Log.d(TAG, "success member benefit response: $it")
            }.onFailure {
                Log.d(TAG, "failed: member benefit ${it.message}")
            }
        }
    }
}
