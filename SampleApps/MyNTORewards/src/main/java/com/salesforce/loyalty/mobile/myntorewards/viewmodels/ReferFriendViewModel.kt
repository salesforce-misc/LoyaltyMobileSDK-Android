package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import com.salesforce.loyalty.mobile.myntorewards.utilities.isValidEmail
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferFriendViewState
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.*

class ReferFriendViewModel(): BaseViewModel<ReferFriendViewState>() {

    init {
        uiMutableState.value = ReferFriendViewState.ReferFriendProgramState(SIGNUP)
    }

    fun onSignUpToReferClicked(email: String) {
        // TODO: Signup to Referral Program with mail id
        uiMutableState.value = ReferFriendViewState.ReferFriendProgramState(JOIN_PROGRAM)
    }

    fun onReferralProgramJoinClicked() {
        uiMutableState.value = ReferFriendViewState.ReferFriendProgramState(START_REFERRING)
    }

    fun sendReferralMail() {
        TODO("Not yet implemented")
    }
}