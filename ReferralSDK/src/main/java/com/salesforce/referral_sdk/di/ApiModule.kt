package com.salesforce.referral_sdk.di

import com.salesforce.referral_sdk.utils.BASE_URL
import com.salesforce.referral_sdk.utils.NETWORK_TIMEOUT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.salesforce.referral_sdk.api.ApiServices
import com.salesforce.referral_sdk.api.ForceAuthenticator
import com.salesforce.referral_sdk.api.UnauthorizedInterceptor
import com.salesforce.referral_sdk.utils.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(unauthorizedInterceptor: UnauthorizedInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(unauthorizedInterceptor)
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
                addInterceptor(loggingInterceptor)
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideUnauthorizedInterceptor(authenticator: ForceAuthenticator)
        = UnauthorizedInterceptor(authenticator)

    @Provides
    @Singleton
    fun provideForceAuthenticator() = ForceAuthManager
}