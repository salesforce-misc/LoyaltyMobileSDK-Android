package com.salesforce.loyalty.mobile.myntorewards.utilities

import com.salesforce.loyalty.mobile.MyNTORewards.R

class ViewPagerSupport {

    companion object ViewPagerSupport {
        fun imageID(page: Int):Int {

            when (page) {
                0 -> {
                    return R.drawable.onboarding_image_1
                }

                1 -> {
                    return R.drawable.onboarding_image_2
                }
                2 -> {
                    return R.drawable.onboarding_image_3
                }
                else->
                {
                    return 0
                }
            }
        }

        fun screenText(page: Int):String {
            when (page) {
                0 -> {
                    return "Get personalized \noffers"
                }

                1 -> {
                    return "Redeem your points for exciting vouchers"
                }
                2 -> {
                    return "Accumulate points and unlock new \n rewards"
                }
                else->
                {
                    return ""
                }
            }
        }

/*
        @Composable
        fun screenText(page: Int):String {
            when (page) {
                0 -> {
                    return stringResource(R.string.onboard_screen_text_1)
                }

                1 -> {
                    return stringResource(R.string.onboard_screen_text_2)
                }
                2 -> {
                    return stringResource(R.string.onboard_screen_text_3)
                }
                else->
                {
                    return ""
                }
            }
        }*/
    }
}