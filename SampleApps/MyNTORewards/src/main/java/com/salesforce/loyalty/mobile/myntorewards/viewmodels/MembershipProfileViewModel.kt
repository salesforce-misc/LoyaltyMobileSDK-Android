package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.models.MembershipProfileModel
import kotlinx.coroutines.launch

class MembershipProfileViewModel: ViewModel() {
    //live data for login status
    val membershipProfileLiveData: LiveData<MembershipProfileModel>
        get() = membershipProfile

    private val membershipProfile = MutableLiveData<MembershipProfileModel>()


    fun getMembershipProfile() {

        viewModelScope.launch {

        }
    }
}