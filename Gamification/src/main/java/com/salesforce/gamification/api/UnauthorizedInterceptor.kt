/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.gamification.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

/**
 * UnauthorizedInterceptor class to handle access token refresh in case of unauthorized errors.
 */
class UnauthorizedInterceptor(auth: GameAuthenticator) : Interceptor {

    val authenticator = auth

    companion object {
        const val BEARER_HEADER = "Bearer "
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response

        val accessToken = authenticator.getAccessToken()
        if (accessToken != null) {
            response = chain.proceed(newRequestWithAccessToken(accessToken, request))
        } else {
            response = chain.proceed(request)
        }
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            var newAccessToken: String? = null
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
            .addHeader(GameAPIConfig.HEADER_AUTHORIZATION, bearerTokenValue)
            .build()
    }
}