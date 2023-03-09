package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class PromotionTabs(val tabName: Int, val selected: Boolean) {
    object TabAll : PromotionTabs(R.string.tab_all, false)
    object TabActive : PromotionTabs(R.string.tab_active, false)
    object TabUnEnrolled : PromotionTabs(R.string.tab_unenrolled, false)
}