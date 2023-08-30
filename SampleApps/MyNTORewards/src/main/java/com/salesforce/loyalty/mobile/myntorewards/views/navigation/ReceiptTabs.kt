package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class ReceiptTabs(val tabName: Int, val selected: Boolean) {
    object EligibleItems : ReceiptTabs(R.string.receipt_tab_eligible_items, false)
    object ReceiptImage : ReceiptTabs(R.string.receipt_tab_receipt_image, false)
}