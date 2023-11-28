package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.annotation.DrawableRes
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.SPIN_A_WHEEL_GAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.SCRATCH_CARD_GAME
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

enum class GameType(val gameType: String, val routeName: String, @DrawableRes val placeHolderId: Int) {
    SPIN_A_WHEEL(SPIN_A_WHEEL_GAME, MoreScreens.SpinWheelScreen.route, R.drawable.placeholder_game_thumbnail),
    SCRATCH_CARD(SCRATCH_CARD_GAME, MoreScreens.ScratchCardScreen.route, R.drawable.placeholder_scratch_card)
}