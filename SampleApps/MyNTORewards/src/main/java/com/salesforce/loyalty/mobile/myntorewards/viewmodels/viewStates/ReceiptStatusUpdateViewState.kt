package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReceiptStatusUpdateViewState {
    object ReceiptStatusUpdateSuccess : ReceiptStatusUpdateViewState()
    object ReceiptStatusUpdateFailure : ReceiptStatusUpdateViewState()
    object ReceiptStatusUpdateInProgress : ReceiptStatusUpdateViewState()
}
