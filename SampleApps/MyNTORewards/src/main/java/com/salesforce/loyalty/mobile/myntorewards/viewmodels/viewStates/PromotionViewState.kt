package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse

sealed class PromotionViewState {

    class PromotionsFetchSuccess(val response: PromotionsResponse?) : PromotionViewState()
    class PromotionsFetchFailure(val message: String?) : PromotionViewState()
    object PromotionFetchInProgress : PromotionViewState()
}