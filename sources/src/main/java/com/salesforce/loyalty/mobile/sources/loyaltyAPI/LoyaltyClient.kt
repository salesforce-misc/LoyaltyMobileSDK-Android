package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceResponseCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ForceClient class is responsible for creating retrofit instance to invoke Force APIs.
 */
class LoyaltyClient constructor(auth: ForceAuthenticator) {

    private val mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val mLoyaltyOkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(UnauthorizedInterceptor(auth))
        .addInterceptor(mHttpLoggingInterceptor)
        .build()


    private val loyaltyRetrofit: Retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(LoyaltyConfig.MEMBER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addCallAdapterFactory(ForceResponseCallAdapterFactory())
        .client(mLoyaltyOkHttpClient)
        .build()

    val loyaltyApi: LoyaltyApiInterface by lazy {
        loyaltyRetrofit.create(LoyaltyApiInterface::class.java)
    }
}