package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReceiptViewState {
    object ReceiptListFetchSuccessView : ReceiptViewState()
    object ReceiptListFetchFailureView : ReceiptViewState()
    object ReceiptListFetchInProgressView : ReceiptViewState()
}
