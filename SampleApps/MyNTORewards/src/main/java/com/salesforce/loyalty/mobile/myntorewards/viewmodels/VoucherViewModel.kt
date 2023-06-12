package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
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
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.VoucherViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResult
import kotlinx.coroutines.launch

class VoucherViewModel : ViewModel(), VoucherViewModelInterface {


    private val TAG = VoucherViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    override val voucherLiveData: LiveData<List<VoucherResponse>>
        get() = vouchers

    private val vouchers = MutableLiveData<List<VoucherResponse>>()


    override val voucherViewState: LiveData<VoucherViewState>
        get() = viewState

    private val viewState = MutableLiveData<VoucherViewState>()

    override fun loadVoucher(context: Context, refreshRequired:Boolean) {
        viewState.postValue(VoucherViewState.VoucherFetchInProgress)
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Logger.d(TAG, "failed: member getVoucher Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val membershipKey = member.membershipNumber ?: ""

            if(refreshRequired)
            {
                getVoucher(context, membershipKey)
            }
            else{
                val voucherCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_VOUCHERS,
                    VoucherResult::class.java
                )

                Logger.d(TAG, "cache : $voucherCache")
                if (voucherCache == null) {
                    getVoucher(context, membershipKey)
                } else {
                    vouchers.value = voucherCache.voucherResponse
                    viewState.postValue(VoucherViewState.VoucherFetchSuccess)
                }
            }

        }
    }
    private fun getVoucher(context: Context, membershipKey: String) {

        viewModelScope.launch {
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
                Logger.d(TAG, "getVoucher success: ${it.voucherResponse}")
            }.onFailure {
                viewState.postValue(VoucherViewState.VoucherFetchFailure)
                Logger.d(TAG, "getVoucher failed: ${it.message}")
            }
        }
    }
}