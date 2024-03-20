package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class BadgeViewState {
    object BadgeFetchSuccess : BadgeViewState()
    object BadgeFetchFailure: BadgeViewState()
    object BadgeFetchInProgress : BadgeViewState()
}