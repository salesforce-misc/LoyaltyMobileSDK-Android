package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class PromotionViewState {

    object PromotionsFetchSuccess : PromotionViewState()
    object PromotionsFetchFailure : PromotionViewState()
    object PromotionFetchInProgress : PromotionViewState()
}