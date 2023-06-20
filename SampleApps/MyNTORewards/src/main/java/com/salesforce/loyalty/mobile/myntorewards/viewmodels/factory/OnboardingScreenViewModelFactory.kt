package com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipBenefitViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OnboardingScreenViewModel
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

class OnboardingScreenViewModelFactory(private val loyaltyAPIManager: LoyaltyAPIManager): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardingScreenViewModel(loyaltyAPIManager) as T
    }
}