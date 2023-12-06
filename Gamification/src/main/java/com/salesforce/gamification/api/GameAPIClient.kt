/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.gamification.api

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * An abstract class NetworkClient which defines the class that configures Retrofit client for invoking the REST APIs.
 */
abstract class NetworkClient(auth: GameAuthenticator, instanceUrl: String) {

    /**
     * Gives an implementation of the retrofit interface using an instance of retrofit.
     *
     * @return [GameAPIInterface]
     */
    abstract fun getNetworkClient(): GameAPIInterface
}

/**
 * GameAPIClient class is responsible for creating retrofit instance to invoke Force APIs.
 */
class GameAPIClient constructor(auth: GameAuthenticator, instanceUrl: String) :
    NetworkClient(auth, instanceUrl) {

    private val unauthorizedInterceptor: UnauthorizedInterceptor = UnauthorizedInterceptor(auth)
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        var mGamificationOkHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        mGamificationOkHttpClient.addInterceptor(unauthorizedInterceptor)
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        mGamificationOkHttpClient.addInterceptor(mHttpLoggingInterceptor)
        return mGamificationOkHttpClient
    }

    private val gamificationRetrofit: Retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(instanceUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addCallAdapterFactory(GameResponseCallAdapterFactory())
        .client(getOkHttpClientBuilder().build())
        .build()

    override fun getNetworkClient(): GameAPIInterface {
        val gamificationApi: GameAPIInterface by lazy {
            gamificationRetrofit.create(GameAPIInterface::class.java)
        }
        return gamificationApi
    }
}