package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

import androidx.annotation.StringRes
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType

sealed class MyReferralsViewState {
    data class MyReferralsFetchSuccess(val uiState: MyReferralScreenState) : MyReferralsViewState()
    data class MyReferralsFetchFailure(val errorMessage: String): MyReferralsViewState()
    object MyReferralsFetchInProgress : MyReferralsViewState()
}

data class MyReferralScreenState(
    val tabItems: List<Int>,
    val completedStates: List<ReferralItemState>,
    val inProgressStates: List<ReferralItemState>,
    val referralsCountList: List<Pair<Int, String>>,
    val referralsRecentDuration: String
)

data class ReferralItemState(
    @StringRes val sectionName: Int,
    val mail: String,
    val duration: String,
    val purchaseStatus: ReferralStatusType
)
