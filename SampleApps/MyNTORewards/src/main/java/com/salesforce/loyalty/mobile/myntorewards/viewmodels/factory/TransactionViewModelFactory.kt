package com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.TransactionsViewModel
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager

class TransactionViewModelFactory(private val loyaltyAPIManager: LoyaltyAPIManager): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionsViewModel(loyaltyAPIManager) as T
    }
}