package com.salesforce.loyalty.mobile.myntorewards.views

import com.salesforce.loyalty.mobile.MyNTORewards.R

sealed class BottomNavTabs(var title:String, var icon:Int, var route:String)
{
    object Home : BottomNavTabs("Home", R.drawable.home_bottombar_icon,"home_screen")
    object MyOffers: BottomNavTabs("Offers",R.drawable.myofffer_bottombar_icon,"my_offer_screen")
    object MyProfile: BottomNavTabs("Profile",R.drawable.myprofile_bottombar_icon,"my_profile_screen")
    object Redeem: BottomNavTabs("Redeem",R.drawable.redeem_bottombar_icon,"redeem_screen")
    object More: BottomNavTabs("More",R.drawable.more_bottombar_icon,"more_screen")
}