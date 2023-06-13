package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceResponseCallAdapterFactory
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyApiInterface
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.NetworkClient
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.UnauthorizedInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockNetworkClient(
    auth: ForceAuthenticator,
    instanceUrl: String,
    mockWebServer: MockWebServer
) :
    NetworkClient(auth, instanceUrl) {
    private val unauthorizedInterceptor: UnauthorizedInterceptor = UnauthorizedInterceptor(auth)
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        var mLoyaltyOkHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        mLoyaltyOkHttpClient.addInterceptor(unauthorizedInterceptor)
        return mLoyaltyOkHttpClient
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(ForceResponseCallAdapterFactory())
            .client(getOkHttpClientBuilder().build())
            .build()

    }

    override fun getNetworkClient(): LoyaltyApiInterface {
        val loyaltyApi: LoyaltyApiInterface by lazy {
            retrofit.create(LoyaltyApiInterface::class.java)
        }
        return loyaltyApi
    }
}