package com.salesforce.loyalty.mobile.myntorewards.checkout.api

import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderCreationRequest
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethodsResponse
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
    ): Result<ShippingMethodsResponse>

    @GET()
    suspend fun getOrderDetails(
        @Url url: String,
        @Query("orderId") orderId: String
    ): Result<OrderDetailsResponse>
}