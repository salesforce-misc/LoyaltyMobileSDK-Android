package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse

sealed class PromotionViewState {

    object PromotionsFetchSuccess : PromotionViewState()
    object PromotionsFetchFailure : PromotionViewState()
    object PromotionFetchInProgress : PromotionViewState()
}