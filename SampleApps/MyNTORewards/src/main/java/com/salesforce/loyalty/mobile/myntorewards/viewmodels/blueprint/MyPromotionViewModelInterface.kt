package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.PromotionEnrollmentUpdateState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

interface MyPromotionViewModelInterface {
    val membershipPromotionLiveData: LiveData<List<Results>>
    val promEnrollmentStatusLiveData: LiveData<PromotionEnrollmentUpdateState>
    val promotionViewState: LiveData<PromotionViewState>
    fun resetPromEnrollmentStatusDefault()
    fun loadPromotions(context: Context, refreshRequired:Boolean=false)
    fun enrollInPromotions(context: Context, promotionName: String)
    fun unEnrollInPromotions(context: Context, promotionName: String)

}