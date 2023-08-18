package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.util.Log
import com.salesforce.loyalty.mobile.myntorewards.checkout.api.CheckoutConfig
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

/**
 * ForceNetworkInterceptor class to handle access token refresh in case of unauthorized errors.
 */
class ForceNetworkInterceptor(auth: ForceAuthenticator) : Interceptor {

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

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request {
        val bearerTokenValue = BEARER_HEADER + accessToken
        return request.newBuilder()
            .addHeader(CheckoutConfig.HEADER_AUTHORIZATION, bearerTokenValue)
            .build()
    }
}