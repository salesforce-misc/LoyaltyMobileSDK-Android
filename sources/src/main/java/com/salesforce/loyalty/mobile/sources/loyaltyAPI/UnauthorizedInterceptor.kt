package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

/**
 * UnauthorizedInterceptor class to handle access token refresh in case of unauthorized errors.
 */
class UnauthorizedInterceptor(auth: ForceAuthenticator) : Interceptor {

    val authenticator = auth

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response

        val accessToken = authenticator.accessToken
        if (accessToken != null) {
            response = chain.proceed(newRequestWithAccessToken(accessToken, request))
        } else {
            response = chain.proceed(request)
        }
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            var newAccessToken: String? = null
            runBlocking {
                authenticator.grantAccessToken().onSuccess {
                    newAccessToken = it.accessToken
                }.onFailure {
                    newAccessToken = null
                }
            }
            newAccessToken?.let {
                return chain.proceed(newRequestWithAccessToken(it, request))
            }
        }
        return response
    }

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .addHeader(LoyaltyConfig.HEADER_AUTHORIZATION, "Bearer $accessToken")
            .build()
}