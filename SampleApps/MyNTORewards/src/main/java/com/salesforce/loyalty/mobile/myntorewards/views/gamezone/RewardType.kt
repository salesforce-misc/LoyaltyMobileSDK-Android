package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants

enum class RewardType(val rewardType: String) {
    REWARD_VOUCHER(AppConstants.VOUCHER),
    NO_VOUCHER(AppConstants.NO_REWARD)
}