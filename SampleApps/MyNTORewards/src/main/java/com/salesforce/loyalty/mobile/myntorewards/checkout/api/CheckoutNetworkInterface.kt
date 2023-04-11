package com.salesforce.loyalty.mobile.myntorewards.checkout.api

import com.salesforce.loyalty.mobile.myntorewards.checkout.models.*
import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse
import retrofit2.http.*

interface CheckoutNetworkInterface {

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

    @POST()
    suspend fun createOrder(
        @Url url: String,
        @Body json: OrderCreationRequest
    ): Result<String>

    @GET()
    suspend fun getShippingMethods(
        @Url url: String,
    ): Result<List<ShippingMethod>>

    @GET()
    suspend fun getOrderDetails(
        @Url url: String,
        @Query("orderId") orderId: String
    ): Result<OrderDetailsResponse>

    @GET()
    suspend fun getShippingBillingAddressSOQL(
        @Url url: String,
        @Query("q") query: String?
    ): Result<QueryResult<ShippingBillingAddressRecord>>
}