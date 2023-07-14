package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class MyProfileViewStates {
    object MyProfileFetchSuccess : MyProfileViewStates()
    object MyProfileFetchFailure: MyProfileViewStates()
    object MyProfileFetchInProgress : MyProfileViewStates()
}