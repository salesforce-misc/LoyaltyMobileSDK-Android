package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReceiptScanningViewState {
    object ReceiptScanningSuccess : ReceiptScanningViewState()
    object ReceiptScanningFailure : ReceiptScanningViewState()
    object ReceiptScanningInProgress : ReceiptScanningViewState()
}
