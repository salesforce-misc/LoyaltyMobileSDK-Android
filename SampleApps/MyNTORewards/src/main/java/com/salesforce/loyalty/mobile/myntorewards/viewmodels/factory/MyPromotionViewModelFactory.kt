package com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

class MyPromotionViewModelFactory(
    private val loyaltyAPIManager: LoyaltyAPIManager,
    private val referralsLocalRepository: ReferralsLocalRepository
): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyPromotionViewModel(loyaltyAPIManager, referralsLocalRepository) as T
    }
}