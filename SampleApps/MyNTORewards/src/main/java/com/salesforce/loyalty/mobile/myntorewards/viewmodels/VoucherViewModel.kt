package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResponse
import kotlinx.coroutines.launch

class VoucherViewModel : ViewModel() {


    private val TAG = VoucherViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(ForceAuthManager)

    val voucherLiveData: LiveData<List<VoucherResponse>>
        get() = vouchers

    private val vouchers = MutableLiveData<List<VoucherResponse>>()


    fun getVoucher(context: Context) {
        viewModelScope.launch {
            var memberID = PrefHelper.customPrefs(context)[AppConstants.KEY_PROGRAM_MEMBER_ID, ""]
            var membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""]
            if (membershipKey != null) {
                loyaltyAPIManager.getVouchers(
                    membershipKey, null, 1, null,
                    null, null, null
                ).onSuccess {
                    vouchers.value = it.voucherResponse
                    Log.d(TAG, "getVoucher success: ${it.voucherResponse}")
                }.onFailure {
                    Log.d(TAG, "getVoucher failed: ${it.message}")
                }
            }
        }
    }
}