package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import com.salesforce.loyalty.mobile.MyNTORewards.BuildConfig

object AppSettings {

    val DEFAULT_FORCE_CONNECTED_APP = ConnectedApp(
        BuildConfig.CONNECTED_APP_NAME,
        BuildConfig.CONSUMER_KEY,
        BuildConfig.CONSUMER_SECRET,
        BuildConfig.CALLBACK_URL,
        BuildConfig.BASE_URL,
        BuildConfig.INSTANCE_URL,
        BuildConfig.COMMUNITY_URL
    )

}