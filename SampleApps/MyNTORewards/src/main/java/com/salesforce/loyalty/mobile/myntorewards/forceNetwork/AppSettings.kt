package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import com.salesforce.loyalty.mobile.MyNTORewards.BuildConfig

object AppSettings {

    const val LOYALTY_PROGRAM_NAME = "VIP Pro"
    const val REWARD_CURRENCY_NAME = "Reward Points"
    const val REWARD_CURRENCY_NAME_SHORT = "Points"
    const val TIER_CURRENCY_NAME = "Tier Points"
    val DEFAULT_FORCE_CONNECTED_APP = ConnectedApp(
        BuildConfig.CONNECTED_APP_NAME,
        BuildConfig.CONSUMER_KEY,
        BuildConfig.CONSUMER_SECRET,
        BuildConfig.CALLBACK_URL,
        BuildConfig.BASE_URL,
        BuildConfig.INSTANCE_URL,
        BuildConfig.COMMUNITY_URL,
        BuildConfig.SELF_REGISTER_URL
    )

}