package com.salesforce.loyalty.mobile.myntorewards.views

import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_HOME_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_MORE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_OFFER_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_PROFILE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.ROUTE_REDEEM_SCREEN

sealed class BottomNavTabs(var titleID: Int, var iconID: Int, var route: String) {
    object Home : BottomNavTabs(
        R.string.screen_title_home,
        R.drawable.home_bottom_bar_icon,
        ROUTE_HOME_SCREEN
    )

    object MyOffers : BottomNavTabs(
        R.string.screen_title_my_offers,
        R.drawable.myoffer_tab_bar_item,
        ROUTE_OFFER_SCREEN
    )

    object MyProfile :
        BottomNavTabs(
            R.string.screen_title_my_profiles,
            R.drawable.myprofile_tab_bar_item,
            ROUTE_PROFILE_SCREEN
        )

    object Redeem : BottomNavTabs(
        R.string.screen_title_redeem,
        R.drawable.redeem_tab_bar_item,
        ROUTE_REDEEM_SCREEN
    )

    object More :
        BottomNavTabs(R.string.screen_title_more, R.drawable.more_tab_bar_item, ROUTE_MORE_SCREEN)
}