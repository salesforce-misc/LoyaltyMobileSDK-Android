package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class GamesViewState {
    object GamesFetchSuccess : GamesViewState()
    object GamesFetchFailure : GamesViewState()
    object GamesFetchInProgress : GamesViewState()
}