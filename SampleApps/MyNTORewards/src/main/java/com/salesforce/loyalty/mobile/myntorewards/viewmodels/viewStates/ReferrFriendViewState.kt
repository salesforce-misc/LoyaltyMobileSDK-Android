package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

import androidx.annotation.StringRes
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs

sealed class ReferFriendViewState {
    data class ReferFriendProgramState(val referralProgramType: ReferralProgramType) : ReferFriendViewState()
    object ShowSignupInvalidEmailMessage : ReferFriendViewState()
    object ReferFriendInProgress : ReferFriendViewState()
    data class ReferFriendSendMailsSuccess(val data: String) : ReferFriendViewState()
}
