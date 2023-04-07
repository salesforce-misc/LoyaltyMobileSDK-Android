package com.salesforce.loyalty.mobile.myntorewards.checkout.api

/**
 * CheckoutAuth class holds the auth token value.
 */
object CheckoutAuth {
    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    fun getAuthToken(): String? {
        return authToken
    }

    fun clearAuthToken() {
        authToken = null
    }
}