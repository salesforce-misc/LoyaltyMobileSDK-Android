package com.salesforce.loyalty.mobile.myntorewards.utilities

import com.salesforce.loyalty.mobile.MyNTORewards.R

class Assets {

    companion object ViewPagerSupport {


        fun getBenefitsLogo(type:String?):Int
        {

            return when (type) {
                "Free Shipping" -> R.drawable.benefit_icon_free_shipping
                "Extended Return" -> R.drawable.benefit_icon_extended_return
                "Free Sample" -> R.drawable.benefit_icon_100_rewards_point
                "Free Subscription" -> R.drawable.benefit_icon_subscription
                "Tier Exclusive Offer" -> R.drawable.benefit_icon_family_benefits
                "Support" -> R.drawable.benefit_icon_support
                "Complimentary Vouchers" -> R.drawable.benefit_icon_personalized_benefits
                else -> R.drawable.benefit_icon_default
            }
        }

    }
}