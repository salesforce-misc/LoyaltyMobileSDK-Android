package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReferFriendViewState {
    object ShowSignupInvalidEmailMessage : ReferFriendViewState()
    object ReferFriendInProgress : ReferFriendViewState()
    object ReferFriendTryAgainInProgress : ReferFriendViewState()
    object ReferFriendSendMailsSuccess : ReferFriendViewState()
    data class ReferFriendSendMailsFailed(val errorMessage: String? = null) : ReferFriendViewState()
    object EnrollmentTaskFinished : ReferFriendViewState()
}
