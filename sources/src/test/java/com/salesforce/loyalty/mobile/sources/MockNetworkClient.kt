package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceResponseCallAdapterFactory
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyApiInterface
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.NetworkClient
import io.reactivex.schedulers.Schedulers
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
    private val retrofit by lazy {
        Retrofit.Builder()
            //1
            .baseUrl(mockWebServer.url("/"))
            //2
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(ForceResponseCallAdapterFactory())
            //4
            .build()

    }

    override fun getNetworkClient(): LoyaltyApiInterface {
        val loyaltyApi: LoyaltyApiInterface by lazy {
            retrofit.create(LoyaltyApiInterface::class.java)
        }
        return loyaltyApi
    }
}