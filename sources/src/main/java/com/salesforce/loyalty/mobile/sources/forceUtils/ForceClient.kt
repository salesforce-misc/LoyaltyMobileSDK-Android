package com.salesforce.loyalty.mobile.sources.forceUtils

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ForceClient class is responsible for creating retrofit instance to invoke Force APIs.
 */
object ForceClient {

    private val mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val mAuthOkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(mHttpLoggingInterceptor)
        .build()

    private val authRetrofit: Retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(ForceConfig.ACCESS_TOKEN_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addCallAdapterFactory(ForceResponseCallAdapterFactory())
        .client(mAuthOkHttpClient)
        .build()

    val authApi: ForceApiInterface by lazy {
        authRetrofit.create(ForceApiInterface::class.java)
    }
}