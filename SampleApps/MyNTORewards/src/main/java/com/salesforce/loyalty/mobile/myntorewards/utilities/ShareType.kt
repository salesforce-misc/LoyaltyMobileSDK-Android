package com.salesforce.loyalty.mobile.myntorewards.utilities

import androidx.annotation.DrawableRes
import com.salesforce.loyalty.mobile.MyNTORewards.R

/**
 * This class holds the all possible social media sharing options
 * To customise the social media icons, change the iconIds configured below
 * To add new social media sharing option, add it to this enum class with all the required details
 */
enum class ShareType(@DrawableRes val iconId: Int, val packageName: String?, val intentShareType: String?) {
    FACEBOOK(R.drawable.ic_facebook, FACEBOOK_APP_PACKAGE, INTENT_TYPE_IMAGE),
    INSTAGRAM(R.drawable.ic_instagram, INSTAGRAM_APP_PACKAGE, INTENT_TYPE_TEXT),
    WHATSAPP(R.drawable.ic_whatsapp, WHATSAPP_APP_PACKAGE, INTENT_TYPE_TEXT),
    TWITTER(R.drawable.ic_twitter, TWITTER_APP_PACKAGE, INTENT_TYPE_TEXT),
    SHARE_OTHERS(R.drawable.ic_share, null, INTENT_TYPE_TEXT) // Package name is null as its generic sharing option
}