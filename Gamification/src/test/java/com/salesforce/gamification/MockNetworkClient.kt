package com.salesforce.gamification

import com.salesforce.gamification.api.*
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockNetworkClient(
    auth: GameAuthenticator,
    instanceUrl: String,
    mockWebServer: MockWebServer
) :
    NetworkClient(auth, instanceUrl) {
    private val unauthorizedInterceptor: UnauthorizedInterceptor = UnauthorizedInterceptor(auth)
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        var mGameOkHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        mGameOkHttpClient.addInterceptor(unauthorizedInterceptor)
        return mGameOkHttpClient
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(GameResponseCallAdapterFactory())
            .client(getOkHttpClientBuilder().build())
            .build()

    }

    override fun getNetworkClient(): GameAPIInterface {
        val gameApi: GameAPIInterface by lazy {
            retrofit.create(GameAPIInterface::class.java)
        }
        return gameApi
    }
}