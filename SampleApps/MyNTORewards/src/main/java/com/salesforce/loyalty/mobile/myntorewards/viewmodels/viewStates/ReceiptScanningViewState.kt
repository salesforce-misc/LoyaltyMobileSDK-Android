package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReceiptScanningViewState {
    object ReceiptScanningSuccess : ReceiptScanningViewState()
    class ReceiptScanningFailure(val message: String?) : ReceiptScanningViewState()
    object ReceiptScanningInProgress : ReceiptScanningViewState()
}
