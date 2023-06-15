package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import com.salesforce.loyalty.mobile.myntorewards.checkout.models.QueryResult
import retrofit2.Call
import retrofit2.http.*

/**
 * ForceApiInterface class holds the RESTAPI call definitions.
 */
interface ForceApiInterface {

    @POST("/services/oauth2/token")
    suspend fun getAccessToken(
        @Header("accept") accept: String,
        @Header("Content-Type") contentType: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Result<ForceAuth>

    @POST()
    suspend fun requestAuthorizationCode(
        @Url url: String,
        @Header("Auth-Request-Type") authRequestType: String,
        @Query("scope") scope: String,
        @Query("response_type") responseType: String,
        @Query("client_id") clientId: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<Any>

    @POST()
    suspend fun requestAccessToken(
        @Url url: String,
        @Query("code") code: String,
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("redirect_uri") redirectUri: String
    ): Result<ForceAuth>

    @POST()
    suspend fun refreshAccessToken(
        @Url url: String,
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("refresh_token") refreshToken: String,
        @Query("client_secret") clientSecret: String?
    ): Result<ForceAuth>

    @POST()
    suspend fun revokeAccessToken(
        @Url url: String,
        @Query("token") accessToken: String
    ): Result<Any?>

    @GET()
    suspend fun getFirstNameLastNameFromContactEmail(
        @Url url: String,
        @Query("q") query: String?,
        @Header("Authorization")bearerToken: String
    ): Result<QueryResult<ContactRecord>>
}