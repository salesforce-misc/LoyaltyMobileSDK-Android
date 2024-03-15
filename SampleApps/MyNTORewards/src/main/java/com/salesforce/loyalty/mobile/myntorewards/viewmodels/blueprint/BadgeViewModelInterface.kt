package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState

interface BadgeViewModelInterface {
    val badgeLiveData: LiveData<LoyaltyBadgeList>
    val badgeViewState: LiveData<BadgeViewState>
    suspend fun loadBadge(context: Context)
    suspend fun loadBadgeDetails(context: Context)
}