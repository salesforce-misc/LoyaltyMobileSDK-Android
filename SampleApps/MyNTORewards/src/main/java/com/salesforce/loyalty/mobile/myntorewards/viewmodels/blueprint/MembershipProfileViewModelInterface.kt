package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyProfileViewStates
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberProfileResponse

interface MembershipProfileViewModelInterface {
    val membershipProfileLiveData: LiveData<MemberProfileResponse?>
    val profileViewState: LiveData<MyProfileViewStates>
    fun loadProfile(context: Context, refreshRequired:Boolean=false)


}