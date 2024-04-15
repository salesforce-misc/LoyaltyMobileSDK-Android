package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class BadgeTabs(override val tabName: Int, override val tabIndex: Int): TabBaseItem(tabName, tabIndex){
    object TabAchieved : BadgeTabs(R.string.badge_text_achieved, 0)
    object TabAvailable : BadgeTabs(R.string.badge_text_available, 1)
    object TabExpired : BadgeTabs(R.string.badge_text_expired, 2)
}