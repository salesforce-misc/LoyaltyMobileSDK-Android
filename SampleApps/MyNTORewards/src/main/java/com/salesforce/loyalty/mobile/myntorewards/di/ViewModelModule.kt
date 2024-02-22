package com.salesforce.loyalty.mobile.myntorewards.di

import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyPromotionViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MyPromotionViewModelInterface
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.referral.repository.ReferralsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideReferralViewModel(
        referralsRepository: ReferralsRepository,
        localRepository: ReferralsLocalRepository,
        instanceUr: String
    ) = MyReferralsViewModel(referralsRepository, localRepository, instanceUr)

    @Provides
    @ViewModelScoped
    fun providePromotionsViewModel(
        loyaltyAPIManager: LoyaltyAPIManager,
        localRepository: ReferralsLocalRepository
    ): MyPromotionViewModelInterface = MyPromotionViewModel(loyaltyAPIManager, localRepository)
}