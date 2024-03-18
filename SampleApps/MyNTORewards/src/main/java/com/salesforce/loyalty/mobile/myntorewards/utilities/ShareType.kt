package com.salesforce.loyalty.mobile.myntorewards.utilities

import androidx.annotation.DrawableRes
import com.salesforce.loyalty.mobile.MyNTORewards.R

/**
 * This class holds the all possible social media sharing options
 * To customise the social media icons, change the iconIds configured below
 * To add new social media sharing option, add it to this enum class with iconId, packageName and Intent share type
 */
enum class ShareType(@DrawableRes val iconId: Int, val packageName: String?, val intentShareType: String?) {
    FACEBOOK(R.drawable.ic_facebook, ShareType.FACEBOOK_APP_PACKAGE, ShareType.INTENT_TYPE_IMAGE),
    INSTAGRAM(R.drawable.ic_instagram, ShareType.INSTAGRAM_APP_PACKAGE, ShareType.INTENT_TYPE_TEXT),
    WHATSAPP(R.drawable.ic_whatsapp, ShareType.WHATSAPP_APP_PACKAGE, ShareType.INTENT_TYPE_TEXT),
    TWITTER(R.drawable.ic_twitter, ShareType.TWITTER_APP_PACKAGE, ShareType.INTENT_TYPE_TEXT),
    SHARE_OTHERS(R.drawable.ic_share, null, ShareType.INTENT_TYPE_TEXT); // Package name is null as its generic sharing option

    companion object {
        const val INTENT_TYPE_TEXT = "text/plain"
        const val INTENT_TYPE_IMAGE = "image/*"
        const val INTENT_TYPE_MAIL = "message/rfc822"
        const val FACEBOOK_APP_PACKAGE = "com.facebook.katana"
        const val WHATSAPP_APP_PACKAGE = "com.whatsapp"
        const val INSTAGRAM_APP_PACKAGE = "com.instagram.android"
        const val TWITTER_APP_PACKAGE = "com.twitter.android"
    }
}