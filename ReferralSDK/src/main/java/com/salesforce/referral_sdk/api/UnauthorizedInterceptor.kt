/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.referral_sdk.api

import com.salesforce.referral_sdk.Logger
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

/**
 * UnauthorizedInterceptor class to handle access token refresh in case of unauthorized errors.
 */
class UnauthorizedInterceptor(private val authenticator: ForceAuthenticator) : Interceptor {
    companion object {
        const val BEARER_HEADER = "Bearer "
        const val HEADER_AUTHORIZATION = "Authorization"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        Logger.d("UnauthorizedInterceptor", "intercept")
        val request = chain.request()
        val response = authenticator.getAccessToken()?.let {
            chain.proceed(newRequestWithAccessToken(it, request))
        } ?: chain.proceed(request)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            var newAccessToken: String?
            // TODO: Need to check exactly y we r using Run blocking here ?
            runBlocking {
                newAccessToken = authenticator.grantAccessToken()
            }
            newAccessToken?.let {
                return chain.proceed(newRequestWithAccessToken(it, request))
            }
        }
        return response
    }

    /**
     * Adds Authorization header to the request.
     *
     * @param accessToken The value of access token that has to be added to the header.
     * @param request The request to which the authorization header has to be added.
     * @return New [Request] with Authorization header added.
     */
    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request {
        val bearerTokenValue = BEARER_HEADER + accessToken
        return request.newBuilder()
            .addHeader(HEADER_AUTHORIZATION, bearerTokenValue)
            .build()
    }
}