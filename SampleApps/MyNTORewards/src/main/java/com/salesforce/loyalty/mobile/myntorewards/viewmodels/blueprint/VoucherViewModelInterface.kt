package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResponse

interface VoucherViewModelInterface {
    val voucherLiveData: LiveData<List<VoucherResponse>>
    val voucherViewState: LiveData<VoucherViewState>
    fun loadVoucher(context: Context, refreshRequired:Boolean=false)

}