package com.salesforce.loyalty.mobile.sources.forceUtils

import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * ForceApiInterface class holds the RESTAPI call definitions.
 */
interface ForceApiInterface {

    @POST("token")
    suspend fun getAccessToken(
        @Header("accept") accept: String,
        @Header("Content-Type") contentType: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Result<ForceAuthResponse>
}