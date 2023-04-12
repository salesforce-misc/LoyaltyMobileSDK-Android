package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class OrderTabs(val tabName: Int, val selected: Boolean) {
    object TabDetails : OrderTabs(R.string.tab_details, false)
    object TabReviews : OrderTabs(R.string.tab_reviews, false)
    object TabTnC : OrderTabs(R.string.tab_tnc, false)
}