package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class ReferralTabs(override val tabName: Int, override val tabIndex: Int): TabBaseItem(tabName, tabIndex) {
    object Success : ReferralTabs(R.string.referral_tab_success, 0)
    object InProgress : ReferralTabs(R.string.referral_tab_in_progress, 1)

    companion object {
        fun sortedTabs(): List<ReferralTabs> = ReferralTabs::class.sealedSubclasses.mapNotNull {
            it.objectInstance
        }.sortedBy { it.tabIndex }
    }
}