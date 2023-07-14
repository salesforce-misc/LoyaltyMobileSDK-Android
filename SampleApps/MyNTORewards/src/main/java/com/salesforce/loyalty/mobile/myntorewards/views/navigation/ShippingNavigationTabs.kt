package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class ShippingNavigationTabs(val tabName: Int, val selected: Boolean) {
    object TabShipping : ShippingNavigationTabs(R.string.tab_shipping, false)
    object TabPayment : ShippingNavigationTabs(R.string.tab_payment, false)
}