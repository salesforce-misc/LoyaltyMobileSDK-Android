package com.salesforce.loyalty.mobile.myntorewards.utilities

import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BENEFIT_TYPE_COMPLIMENT_VOUCHER
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BENEFIT_TYPE_EXTENDED_RETURN
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BENEFIT_TYPE_FREE_SAMPLE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BENEFIT_TYPE_FREE_SHIPPING
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BENEFIT_TYPE_FREE_SUBSCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BENEFIT_TYPE_OFFER
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BENEFIT_TYPE_SUPPORT

class Assets {

    companion object ViewPagerSupport {
        fun getBenefitsLogo(type:String?):Int
        {
            return when (type) {
                BENEFIT_TYPE_FREE_SHIPPING -> R.drawable.benefit_icon_free_shipping
                BENEFIT_TYPE_EXTENDED_RETURN -> R.drawable.benefit_icon_extended_return
                BENEFIT_TYPE_FREE_SAMPLE -> R.drawable.benefit_icon_100_rewards_point
                BENEFIT_TYPE_FREE_SUBSCRIPTION -> R.drawable.benefit_icon_subscription
                BENEFIT_TYPE_OFFER -> R.drawable.benefit_icon_family_benefits
                BENEFIT_TYPE_SUPPORT -> R.drawable.benefit_icon_support
                BENEFIT_TYPE_COMPLIMENT_VOUCHER -> R.drawable.benefit_icon_personalized_benefits
                else -> R.drawable.benefit_icon_default
            }
        }
    }
}