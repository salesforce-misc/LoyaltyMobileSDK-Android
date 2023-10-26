package com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

class GameViewModelFactory(private val loyaltyAPIManager: LoyaltyAPIManager): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(loyaltyAPIManager) as T
    }
}