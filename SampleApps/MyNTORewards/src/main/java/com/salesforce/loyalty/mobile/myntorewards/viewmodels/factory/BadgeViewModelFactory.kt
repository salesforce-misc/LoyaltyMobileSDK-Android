package com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salesforce.loyalty.mobile.myntorewards.badge.LoyaltyBadgeManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.BadgeViewModel

class BadgeViewModelFactory(private val badgeManager: LoyaltyBadgeManager) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BadgeViewModel(badgeManager) as T
    }
}
