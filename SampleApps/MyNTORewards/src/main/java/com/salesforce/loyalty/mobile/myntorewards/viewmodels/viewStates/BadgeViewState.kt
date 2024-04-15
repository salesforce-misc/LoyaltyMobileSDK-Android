package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class BadgeViewState {
    object BadgeFetchSuccess : BadgeViewState()
    data class BadgeFetchFailure(val errorMessage: String? = null): BadgeViewState()
    object BadgeFetchInProgress : BadgeViewState()
}