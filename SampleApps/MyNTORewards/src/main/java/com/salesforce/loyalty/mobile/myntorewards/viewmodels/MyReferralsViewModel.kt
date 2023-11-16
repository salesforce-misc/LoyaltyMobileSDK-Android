package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferralItemState
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyReferralsViewModel(): BaseViewModel<MyReferralsViewState>() {

    init {
        fetchReferralsInfo()
    }

    private fun fetchReferralsInfo() {
        _uiState.value = MyReferralsViewState.MyReferralsFetchInProgress
        viewModelScope.launch {
            delay(2000) // TODO: REMOVE
            _uiState.value = MyReferralsViewState.MyReferralsFetchSuccess(successState())
        }
    }

    private fun successState() = MyReferralScreenState(
        tabItems = listOf(ReferralTabs.Success.tabName, ReferralTabs.InProgress.tabName),
        completedStates = referralItemStates(),
        inProgressStates = referralItemStatesInProgress(),
        listOf(Pair(R.string.my_referral_sent_label, "18"),
            Pair(R.string.my_referrals_accepted_label, "12"),
            Pair(R.string.my_referrals_vouchers_earned_label, "12"),
            Pair(R.string.my_referrals_points_earned_label, "1200"))
    )

    fun referralItemStates(): ArrayList<ReferralItemState> {
        val referralItemStates = arrayListOf(
            ReferralItemState(
                R.string.recent_referrals_section_name,
                "strawberry.sheikh@yahoo.com",
                "2 days ago",
                ReferralStatusType.COMPLETED
            ),
            ReferralItemState(
                R.string.referral_one_month_ago_section_name,
                "strawberry.sheikh@yahoo.com",
                "2 days ago",
                ReferralStatusType.COMPLETED
            ),
            ReferralItemState(
                R.string.referrals_older_than_three_months_section_name,
                "strawberry.sheikh@yahoo.com",
                "2 days ago",
                ReferralStatusType.COMPLETED
            ),
            ReferralItemState(
                R.string.recent_referrals_section_name,
                "strawberry.sheikh@yahoo.com",
                "2 days ago",
                ReferralStatusType.COMPLETED
            )
        )
        return referralItemStates
    }
    fun referralItemStatesInProgress(): ArrayList<ReferralItemState> {
        val referralItemStates = arrayListOf(
            ReferralItemState(
                R.string.recent_referrals_section_name,
                "strawberry.sheikh@yahoo.com",
                "2 days ago",
                ReferralStatusType.PENDING
            ),
            ReferralItemState(
                R.string.referral_one_month_ago_section_name,
                "strawberry.sheikh@yahoo.com",
                "2 days ago",
                ReferralStatusType.SIGNED_UP
            ),
            ReferralItemState(
                R.string.referrals_older_than_three_months_section_name,
                "strawberry.sheikh@yahoo.com",
                "2 days ago",
                ReferralStatusType.PENDING
            ),
            ReferralItemState(R.string.recent_referrals_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.SIGNED_UP)
        )
        return referralItemStates
    }
}