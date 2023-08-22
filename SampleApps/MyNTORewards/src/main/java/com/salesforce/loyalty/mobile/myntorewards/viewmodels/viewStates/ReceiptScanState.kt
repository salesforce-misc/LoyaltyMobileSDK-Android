package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReceiptScanState {
    object ReceiptListFetchSuccess : ReceiptScanState()
    object ReceiptListFetchFailure : ReceiptScanState()
    object ReceiptListFetchInProgress : ReceiptScanState()
}
