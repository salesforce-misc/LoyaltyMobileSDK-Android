package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants

enum class RewardType(val rewardType: String) {
    NO_VOUCHER(AppConstants.NO_REWARD),
    REWARD_VOUCHER(AppConstants.REWARD_TYPE_VOUCHER),
    REWARD_TYPE_POINTS(AppConstants.REWARD_TYPE_POINTS),
    REWARD_TYPE_CUSTOM(AppConstants.REWARD_TYPE_CUSTOM),

}