package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGreen

enum class ReferralStatusType(val status: String, @StringRes val content: Int, @DrawableRes val iconId: Int, @ColorRes val contentColor: Color) {
    COMPLETED("Friend Completes First Purchase", R.string.purchase_status_completed, R.drawable.ic_success, TextGreen),
    PENDING("Advocate Refers Friend", R.string.purchase_status_pending, R.drawable.ic_pending_referral, Color.Black),
    SIGNED_UP("Friend Signs Up", R.string.purchase_status_signed_up, R.drawable.ic_signed_up_referral, Color.Black),
    UNKNOWN("", R.string.purchase_status_pending, R.drawable.ic_signed_up_referral, Color.Black);

    companion object {
        infix fun from(value: String?): ReferralStatusType
            = ReferralStatusType.values().firstOrNull { it.status == value } ?: UNKNOWN
    }
}