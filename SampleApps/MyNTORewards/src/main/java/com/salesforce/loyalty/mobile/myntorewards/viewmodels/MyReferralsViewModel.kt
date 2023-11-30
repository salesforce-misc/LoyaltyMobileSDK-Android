package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.isValidEmail
import com.salesforce.loyalty.mobile.myntorewards.utilities.sendMail
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferralItemState
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting

class MyReferralsViewModel(): BaseViewModel<MyReferralsViewState>() {

    var emailFieldState by mutableStateOf(TextFieldValue(""))
        private set

    init {
        fetchReferralsInfo()
    }

    private fun fetchReferralsInfo() {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            delay(2000) // TODO: REMOVE
            updateSuccessState(successState())
        }
    }

    @VisibleForTesting
    private fun updateSuccessState(successState: MyReferralScreenState) {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchSuccess(successState))
    }

    fun updateEmailTextField(input: TextFieldValue) {
        emailFieldState = input
    }

    // TODO: REMOVE MOCK DATA ONCE THE REAL API IS INTEGRATED
    private fun successState() = MyReferralScreenState(
        tabItems = listOf(ReferralTabs.Success.tabName, ReferralTabs.InProgress.tabName),
        completedStates = completedItemStates(),
        inProgressStates = inProgressItemStates(),
        listOf(Pair(R.string.my_referral_sent_label, "18"),
            Pair(R.string.my_referrals_accepted_label, "12"),
            Pair(R.string.my_referrals_vouchers_earned_label, "12"),
            Pair(R.string.my_referrals_points_earned_label, "1200")),
        referralsRecentDuration = "90"
    )

    private fun completedItemStates(): List<ReferralItemState> {
        return listOf(
            ReferralItemState(R.string.recent_referrals_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.COMPLETED),
            ReferralItemState(R.string.referral_one_month_ago_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.COMPLETED),
            ReferralItemState(R.string.referrals_older_than_three_months_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.COMPLETED),
            ReferralItemState(R.string.recent_referrals_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.COMPLETED)
        )
    }

    private fun inProgressItemStates(): List<ReferralItemState> {
        return listOf(
            ReferralItemState(R.string.recent_referrals_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.PENDING),
            ReferralItemState(R.string.referral_one_month_ago_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.SIGNED_UP),
            ReferralItemState(R.string.referrals_older_than_three_months_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.PENDING),
            ReferralItemState(R.string.recent_referrals_section_name, "strawberry.sheikh@yahoo.com", "2 days ago", ReferralStatusType.SIGNED_UP)
        )
    }
}