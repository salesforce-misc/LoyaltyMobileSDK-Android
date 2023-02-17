package com.salesforce.loyalty.mobile.myntorewards.views

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class BottomNavTabs(var title: String, var icon: Int, var route: String) {
    object Home : BottomNavTabs("Home", R.drawable.home_bottom_bar_icon, "home_screen")
    object MyOffers : BottomNavTabs("Offers", R.drawable.myoffer_tab_bar_item, "my_offer_screen")
    object MyProfile :
        BottomNavTabs("Profile", R.drawable.myprofile_tab_bar_item, "my_profile_screen")

    object Redeem : BottomNavTabs("Redeem", R.drawable.redeem_tab_bar_item, "redeem_screen")
    object More : BottomNavTabs("More", R.drawable.more_tab_bar_item, "more_screen")
}