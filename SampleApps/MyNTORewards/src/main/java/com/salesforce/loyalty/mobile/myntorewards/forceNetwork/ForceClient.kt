package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import com.salesforce.loyalty.mobile.sources.BuildConfig
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
object ForceClient {
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        var mAuthOkHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.LOG_ENABLED) {
            val mHttpLoggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            mAuthOkHttpClient.addInterceptor(mHttpLoggingInterceptor)
        }
        return mAuthOkHttpClient
    }

    private val authRetrofit: Retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(ForceConfig.ACCESS_TOKEN_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addCallAdapterFactory(ForceResponseCallAdapterFactory())
        .client(getOkHttpClientBuilder().build())
        .build()

    val authApi: ForceApiInterface by lazy {
        authRetrofit.create(ForceApiInterface::class.java)
    }
}