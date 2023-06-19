package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BenefitViewStates
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit

interface BenefitViewModelInterface {
    val membershipBenefitLiveData: LiveData<List<MemberBenefit>>
    val benefitViewState: LiveData<BenefitViewStates>
    fun loadBenefits(context: Context, refreshRequired:Boolean=false)
}