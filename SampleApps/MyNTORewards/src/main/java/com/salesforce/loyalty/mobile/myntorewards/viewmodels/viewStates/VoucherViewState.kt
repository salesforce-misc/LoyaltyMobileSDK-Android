package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates



sealed class VoucherViewState {
    object VoucherFetchSuccess : VoucherViewState()
    object VoucherFetchFailure: VoucherViewState()
    object VoucherFetchInProgress : VoucherViewState()
}