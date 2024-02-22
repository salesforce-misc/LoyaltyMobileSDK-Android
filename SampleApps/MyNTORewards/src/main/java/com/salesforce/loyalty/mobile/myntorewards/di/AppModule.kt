package com.salesforce.loyalty.mobile.myntorewards.di

import android.content.Context
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralForceAuthenticatorImpl
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.gamification.api.GameAPIClient
import com.salesforce.gamification.api.NetworkClient
import com.salesforce.gamification.repository.GamificationRemoteRepository
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyClient
import com.salesforce.referral.api.ReferralForceAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideForceAuthManager(@ApplicationContext context: Context): ForceAuthManager {
        return ForceAuthManager(context)
    }

    @Provides
    fun provideInstanceUrl(forceAuthManager: ForceAuthManager): String {
        return forceAuthManager.getInstanceUrl()
            ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    }

    @Provides
    fun provideAPiService(retrofit: Retrofit): ReferralsLocalApiService =
        retrofit.create(ReferralsLocalApiService::class.java)

    @Provides
    @Singleton
    fun provideReferralForceAuthenticatorImpl(forceAuthManager: ForceAuthManager): ReferralForceAuthenticator =
        ReferralForceAuthenticatorImpl(forceAuthManager)

    @Provides
    fun provideNetworkClient(
        forceAuthManager: ForceAuthManager,
        instanceUrl: String
    ): NetworkClient {
        return GameAPIClient(forceAuthManager, instanceUrl)
    }

    @Provides
    fun provideLoyaltyNetworkClient(
        forceAuthManager: ForceAuthManager,
        instanceUrl: String
    ): com.salesforce.loyalty.mobile.sources.loyaltyAPI.NetworkClient {
        return LoyaltyClient(forceAuthManager, instanceUrl)
    }

    @Provides
    @Singleton
    fun provideForceAuthenticatorImpl(@ApplicationContext context: Context): ForceAuthenticator =
        ForceAuthManager(context)

    @Provides
    fun provideLoyaltyApiManager(
        auth: ForceAuthenticator,
        instanceUrl: String,
        loyaltyClient: com.salesforce.loyalty.mobile.sources.loyaltyAPI.NetworkClient
    ): LoyaltyAPIManager {
        return LoyaltyAPIManager(auth, instanceUrl, loyaltyClient)
    }

    @Provides
    fun provideGamificationRemoteRepository(
        forceAuthManager: ForceAuthManager,
        instanceUrl: String,
        gameClient: NetworkClient
    ): GamificationRemoteRepository {
        return GamificationRemoteRepository(forceAuthManager, instanceUrl, gameClient)
    }

    @Provides
    @Singleton
    fun provideReferralsLocalRepository(
        referralsLocalApiService: ReferralsLocalApiService,
        instanceUrl: String
    ): ReferralsLocalRepository {
        return ReferralsLocalRepository(referralsLocalApiService, instanceUrl)
    }
}