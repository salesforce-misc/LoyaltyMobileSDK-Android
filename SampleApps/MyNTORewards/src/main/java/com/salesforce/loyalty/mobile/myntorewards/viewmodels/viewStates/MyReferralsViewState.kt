package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

import androidx.annotation.StringRes
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs

sealed class MyReferralsViewState {
    data class MyReferralsFetchSuccess(val uiState: MyReferralScreenState) : MyReferralsViewState()
    data class MyReferralsFetchFailure(val errorMessage: String? = null): MyReferralsViewState()
    object MyReferralsFetchInProgress : MyReferralsViewState()
    object MyReferralsPromotionEnrolled : MyReferralsViewState()
    object MyReferralsPromotionNotEnrolled : MyReferralsViewState()
    data class MyReferralsPromotionStatusFailure(val errorMessage: String? = null): MyReferralsViewState()
    object PromotionStateNonReferral : MyReferralsViewState()
    data class PromotionReferralApiStatusFailure(val error: String? = null) : MyReferralsViewState()
}

data class MyReferralScreenState(
    val tabItems: List<ReferralTabs> = emptyList(),
    val completedStates: List<ReferralItemState> = emptyList(),
    val inProgressStates: List<ReferralItemState> = emptyList(),
    val referralsCountList: List<Pair<Int, String>>,
    val referralsRecentDuration: String
)

data class ReferralItemState(
    @StringRes val sectionName: Int,
    val mail: String,
    val duration: String,
    val purchaseStatus: ReferralStatusType
)
