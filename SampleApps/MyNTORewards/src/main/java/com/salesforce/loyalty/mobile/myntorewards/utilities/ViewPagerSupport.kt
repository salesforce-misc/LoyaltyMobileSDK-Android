package com.salesforce.loyalty.mobile.myntorewards.utilities

import com.salesforce.loyalty.mobile.MyNTORewards.R

class ViewPagerSupport {
    companion object ViewPagerSupport {
        fun imageID(page: Int): Int {
            return when (page) {
                0 -> R.drawable.onboarding_image_2
                1 -> R.drawable.onboarding_image_3
                2 -> R.drawable.onboarding_image_1
                else -> 0
            }
        }

        fun screenTextID(page: Int): Int {
            return when (page) {
                0 -> R.string.onboard_screen_text_2
                1 -> R.string.onboard_screen_text_3
                2 -> R.string.onboard_screen_text_1
                else -> R.string.onboard_screen_text_1
            }
        }
    }
}