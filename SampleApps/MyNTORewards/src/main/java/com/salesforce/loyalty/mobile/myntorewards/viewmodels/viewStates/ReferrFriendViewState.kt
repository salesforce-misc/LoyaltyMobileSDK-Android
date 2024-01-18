package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReferFriendViewState {
    object ShowSignupInvalidEmailMessage : ReferFriendViewState()
    object ReferFriendInProgress : ReferFriendViewState()
    object ReferFriendTryAgainInProgress : ReferFriendViewState()
    data class ReferFriendSendMailsSuccess(val data: String) : ReferFriendViewState()
    data class EnrollmentFailed(val errorMessage: String? = null) : ReferFriendViewState()
}
