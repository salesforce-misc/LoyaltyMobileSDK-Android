package com.salesforce.loyalty.mobile.myntorewards.views.navigation

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class GameZoneTabs(val tabName: Int, val selected: Boolean) {
    object ActiveGames : GameZoneTabs(R.string.tab_game_zone_available, false)
    object ExpiredGames : GameZoneTabs(R.string.tab_game_zone_expired, false)
    object PlayedGames : GameZoneTabs(R.string.tab_game_zone_played, false)
}