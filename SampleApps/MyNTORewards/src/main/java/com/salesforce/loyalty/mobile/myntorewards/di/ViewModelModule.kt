package com.salesforce.loyalty.mobile.myntorewards.di

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
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
        forceAuthManager: ForceAuthManager
    ) = MyReferralsViewModel(referralsRepository, localRepository, forceAuthManager)
}