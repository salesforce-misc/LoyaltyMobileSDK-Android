package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class GameRewardViewState {
    object GameRewardFetchSuccess : GameRewardViewState()
    object GameRewardFetchFailure : GameRewardViewState()
    object GameRewardFetchInProgress : GameRewardViewState()
}