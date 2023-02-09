package com.salesforce.loyalty.mobile.sources.forceUtils

/**
 * ForceAuth class holds the auth token value.
 */
object ForceAuth {
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