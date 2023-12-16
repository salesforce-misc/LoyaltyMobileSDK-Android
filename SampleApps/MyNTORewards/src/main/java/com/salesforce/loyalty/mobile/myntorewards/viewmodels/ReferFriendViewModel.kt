package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.utilities.isValidEmail
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferFriendViewState
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReferFriendViewModel(): BaseViewModel<ReferFriendViewState>() {

    private val _programState: MutableLiveData<ReferralProgramType> = MutableLiveData()
    val programState: LiveData<ReferralProgramType> = _programState

    fun updateInitialState(referralProgramType: ReferralProgramType) {
        _programState.value = referralProgramType
    }

    fun onSignUpToReferClicked(email: String) {
        // TODO: Signup to Referral Program with mail id
        _programState.value = JOIN_PROGRAM
    }

    fun onReferralProgramJoinClicked() {
        _programState.value = START_REFERRING
    }

    fun sendReferralMail(emails: List<String>) {
        /*if (emails.isEmpty() || emails.any { !isValidEmail(it.trim()) }) {
            uiMutableState.value = ReferFriendViewState.ShowSignupInvalidEmailMessage
//            Toast.makeText(this, getString(R.string.emails_warning_message_multiple), Toast.LENGTH_LONG).show()
            return
        }*/
        uiMutableState.value = ReferFriendViewState.ReferFriendInProgress
        viewModelScope.launch {
            delay(2000) // TODO: REMOVE
            uiMutableState.value = ReferFriendViewState.ReferFriendSendMailsSuccess("")
        }
    }
}