package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.PromotionEnrollmentUpdateState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionClickState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse

interface MyPromotionViewModelInterface {
    val membershipPromotionLiveData: LiveData<PromotionsResponse>
    val promEnrollmentStatusLiveData: LiveData<PromotionEnrollmentUpdateState>
    val promotionViewState: LiveData<PromotionViewState>
    val promotionClickState: LiveData<PromotionClickState>
    fun resetPromEnrollmentStatusDefault()
    fun loadPromotions(context: Context, refreshRequired:Boolean=false)
    fun enrollInPromotions(context: Context, promotionName: String)
    fun unEnrollInPromotions(context: Context, promotionName: String)
}