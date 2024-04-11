package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramMemberBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState

interface BadgeViewModelInterface {
    val programMemberBadgeLiveData: LiveData<LoyaltyBadgeList<LoyaltyProgramMemberBadgeListRecord>>
    val programBadgeLiveData: LiveData<LoyaltyBadgeList<LoyaltyProgramBadgeListRecord>>
    val badgeProgramViewState: LiveData<BadgeViewState>

    val badgeProgramMemberViewState: LiveData<BadgeViewState>
    fun loadLoyaltyProgramMemberBadge(context: Context)
    fun loadLoyaltyProgramBadge(context: Context)
}