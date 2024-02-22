package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class PromotionClickState {
    object PromotionStateNonReferral : PromotionClickState()
    data class PromotionStateReferral(
        val isUserEnrolledToReferralPromotion: Boolean?,
        val promotionCode: String?
    ) : PromotionClickState()
    data class PromotionReferralApiStatusFailure(val error: String? = null) : PromotionClickState()
    object PromotionReferralApiStatusInProgress : PromotionClickState()
    object PromotionStateReferralHideSheet : PromotionClickState()
}