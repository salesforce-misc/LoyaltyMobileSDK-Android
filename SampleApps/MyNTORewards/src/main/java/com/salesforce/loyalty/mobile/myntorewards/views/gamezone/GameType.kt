package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.SPIN_A_WHEEL_GAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.SCRATCH_CARD_GAME

enum class GameType(val gameType: String) {
    SPIN_A_WHEEL(SPIN_A_WHEEL_GAME),
    SCRATCH_CARD(SCRATCH_CARD_GAME)
}