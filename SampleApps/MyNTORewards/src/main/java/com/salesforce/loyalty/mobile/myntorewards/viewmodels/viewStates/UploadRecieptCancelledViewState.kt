package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class UploadRecieptCancelledViewState {
    object  UploadRecieptCancelledSuccess : UploadRecieptCancelledViewState()
    object  UploadRecieptCancelledFailure : UploadRecieptCancelledViewState()
    object  UploadRecieptCancelledInProgress : UploadRecieptCancelledViewState()
}