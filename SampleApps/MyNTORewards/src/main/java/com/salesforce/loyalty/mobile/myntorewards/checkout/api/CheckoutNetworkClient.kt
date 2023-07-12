package com.salesforce.loyalty.mobile.myntorewards.checkout.api

import com.salesforce.loyalty.mobile.sources.BuildConfig
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceResponseCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CheckoutNetworkClient constructor(auth: ForceAuthenticator, instanceUrl: String) {

    private val unauthorizedInterceptor: CheckoutNetworkInterceptor = CheckoutNetworkInterceptor(auth)
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        var mAuthOkHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        mAuthOkHttpClient.addInterceptor(unauthorizedInterceptor)
        if (BuildConfig.LOG_ENABLED) {
            val mHttpLoggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            mAuthOkHttpClient.addInterceptor(mHttpLoggingInterceptor)
        }
        return mAuthOkHttpClient
    }

    private val authRetrofit: Retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(instanceUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addCallAdapterFactory(ForceResponseCallAdapterFactory())
        .client(getOkHttpClientBuilder().build())
        .build()

    val authApi: CheckoutNetworkInterface by lazy {
        authRetrofit.create(CheckoutNetworkInterface::class.java)
    }
}