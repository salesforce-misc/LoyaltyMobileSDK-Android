package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class ReferralTabs(val tabName: Int, val selected: Boolean) {
    object Success : ReferralTabs(R.string.referral_tab_success, false)
    object InProgress : ReferralTabs(R.string.referral_tab_in_progress, false)
}