package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R


sealed class VoucherTabs(val tabName: Int, val selected: Boolean) {
    object TabAvailable : VoucherTabs(R.string.voucher_text_available, false)
    object TabRedeemed : VoucherTabs(R.string.voucher_text_redeemed, false)
    object TabExpired : VoucherTabs(R.string.voucher_text_expired, false)
}