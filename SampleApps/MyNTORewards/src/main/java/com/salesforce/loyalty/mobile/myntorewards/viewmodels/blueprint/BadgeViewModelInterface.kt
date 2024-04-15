package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeListProgramMember
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeProgramList
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState

interface BadgeViewModelInterface {
    val programMemberBadgeLiveData: LiveData<LoyaltyBadgeListProgramMember>
    val programBadgeLiveData: LiveData<LoyaltyBadgeProgramList>
    val badgeProgramViewState: LiveData<BadgeViewState>

    val badgeProgramMemberViewState: LiveData<BadgeViewState>
    fun loadLoyaltyProgramMemberBadge(context: Context, membershipKey:String)
    fun loadLoyaltyProgramBadge(context: Context, membershipKey:String)
    fun getCahchedProgramMemberBadge(context: Context, refreshRequired:Boolean=false)
    fun getCahchedProgramBadge(context: Context, refreshRequired:Boolean=false)
}