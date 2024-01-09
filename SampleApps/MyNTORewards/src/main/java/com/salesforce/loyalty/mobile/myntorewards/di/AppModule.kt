package com.salesforce.loyalty.mobile.myntorewards.di

import android.content.Context
import com.salesforce.gamification.api.GameAPIClient
import com.salesforce.gamification.api.NetworkClient
import com.salesforce.gamification.repository.GamificationRemoteRepository
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


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
    fun provideNetworkClient(
        forceAuthManager: ForceAuthManager,
        instanceUrl: String
    ): NetworkClient {
        return GameAPIClient(forceAuthManager, instanceUrl)
    }

    @Provides
    fun provideGamificationRemoteRepository(
        forceAuthManager: ForceAuthManager,
        instanceUrl: String,
        gameClient: NetworkClient
    ): GamificationRemoteRepository {
        return GamificationRemoteRepository(forceAuthManager, instanceUrl, gameClient)
    }
}