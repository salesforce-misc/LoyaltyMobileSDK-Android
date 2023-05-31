/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.salesforce.loyalty.mobile.sources.BuildConfig
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceResponseCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class NetworkClient(auth: ForceAuthenticator, instanceUrl: String) {
    abstract fun getNetworkClient(): LoyaltyApiInterface
}

/**
 * ForceClient class is responsible for creating retrofit instance to invoke Force APIs.
 */
class LoyaltyClient constructor(auth: ForceAuthenticator, instanceUrl: String): NetworkClient(auth, instanceUrl) {

    private val unauthorizedInterceptor: UnauthorizedInterceptor = UnauthorizedInterceptor(auth)
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        var mLoyaltyOkHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        mLoyaltyOkHttpClient.addInterceptor(unauthorizedInterceptor)
        if (BuildConfig.LOG_ENABLED) {
            val mHttpLoggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            mLoyaltyOkHttpClient.addInterceptor(mHttpLoggingInterceptor)
        }
        return mLoyaltyOkHttpClient
    }

    private val loyaltyRetrofit: Retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(instanceUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addCallAdapterFactory(ForceResponseCallAdapterFactory())
        .client(getOkHttpClientBuilder().build())
        .build()

    /*val loyaltyApi: LoyaltyApiInterface by lazy {
        loyaltyRetrofit.create(LoyaltyApiInterface::class.java)
    }*/

    override fun getNetworkClient(): LoyaltyApiInterface {
        val loyaltyApi: LoyaltyApiInterface by lazy {
            loyaltyRetrofit.create(LoyaltyApiInterface::class.java)
        }
        return loyaltyApi
    }
}