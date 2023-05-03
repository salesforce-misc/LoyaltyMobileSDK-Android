package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class BenefitViewStates {
    object BenefitFetchSuccess : BenefitViewStates()
    object BenefitFetchFailure: BenefitViewStates()
    object BenefitFetchInProgress : BenefitViewStates()
}