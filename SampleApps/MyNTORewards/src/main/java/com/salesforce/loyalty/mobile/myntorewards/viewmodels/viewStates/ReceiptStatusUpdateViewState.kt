package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReceiptStatusUpdateViewState {
    class ReceiptStatusUpdateSuccess(val points: String?) : ReceiptStatusUpdateViewState()
    object ReceiptStatusUpdateFailure : ReceiptStatusUpdateViewState()
    object ReceiptStatusUpdateInProgress : ReceiptStatusUpdateViewState()
}
