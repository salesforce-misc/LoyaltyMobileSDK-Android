package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants

enum class ParticipantRewardStatus(val status: String) {
    YET_TO_REWARD(AppConstants.REWARD_STATUS_YET_TO_REWARD),
    REWARDED(AppConstants.REWARD_STATUS_REWARDED),
    NO_REWARD(AppConstants.REWARD_STATUS_NO_REWARD),
    EXPIRED(AppConstants.REWARD_STATUS_EXPIRED)
}