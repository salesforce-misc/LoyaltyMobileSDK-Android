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
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResult
import kotlinx.coroutines.launch

class VoucherViewModel : ViewModel() {


    private val TAG = VoucherViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    val voucherLiveData: LiveData<List<VoucherResponse>>
        get() = vouchers

    private val vouchers = MutableLiveData<List<VoucherResponse>>()


    val voucherViewState: LiveData<VoucherViewState>
        get() = viewState

    private val viewState = MutableLiveData<VoucherViewState>()

    fun loadVoucher(context: Context) {
        viewState.postValue(VoucherViewState.VoucherFetchInProgress)
        viewModelScope.launch {
            val membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""] ?: ""
            val voucherCache = LocalFileManager.getData(
                context,
                membershipKey,
                LocalFileManager.DIRECTORY_VOUCHERS,
                VoucherResult::class.java
            )

            Log.d(TAG, "cache : $voucherCache")
            if (voucherCache == null) {
                getVoucher(context)
            } else {
                vouchers.value = voucherCache.voucherResponse
                viewState.postValue(VoucherViewState.VoucherFetchSuccess)
            }
        }
    }
    fun getVoucher(context: Context) {
        viewState.postValue(VoucherViewState.VoucherFetchInProgress)
        viewModelScope.launch {
            var memberID = PrefHelper.customPrefs(context)[AppConstants.KEY_PROGRAM_MEMBER_ID, ""]
            var membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""]
            if (membershipKey != null) {
                loyaltyAPIManager.getVouchers(
                    membershipKey, null, 1, null,
                    null, null, null
                ).onSuccess {
                    LocalFileManager.saveData(
                        context,
                        it,
                        membershipKey,
                        LocalFileManager.DIRECTORY_VOUCHERS
                    )

                    vouchers.value = it.voucherResponse
                    viewState.postValue(VoucherViewState.VoucherFetchSuccess)
                    Log.d(TAG, "getVoucher success: ${it.voucherResponse}")
                }.onFailure {
                    viewState.postValue(VoucherViewState.VoucherFetchFailure)
                    Log.d(TAG, "getVoucher failed: ${it.message}")
                }
            }
        }
    }
}