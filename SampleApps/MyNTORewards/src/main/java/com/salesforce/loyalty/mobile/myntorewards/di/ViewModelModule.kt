package com.salesforce.loyalty.mobile.myntorewards.di

import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.referral_sdk.repository.ReferralsRepository
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
        referralsRepository: ReferralsRepository
    ) = MyReferralsViewModel(referralsRepository)
}