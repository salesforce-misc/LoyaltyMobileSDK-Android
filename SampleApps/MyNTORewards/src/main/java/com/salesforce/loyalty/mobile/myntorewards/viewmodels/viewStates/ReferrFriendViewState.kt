package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class ReferFriendViewState {
    object ShowSignupInvalidEmailMessage : ReferFriendViewState()
    object ReferFriendInProgress : ReferFriendViewState()
    object ReferFriendTryAgainInProgress : ReferFriendViewState()
    object ReferFriendSendMailsSuccess : ReferFriendViewState()
    object ReferFriendSendMailsFailed : ReferFriendViewState()
    object EnrollmentTaskFinished : ReferFriendViewState()
}
