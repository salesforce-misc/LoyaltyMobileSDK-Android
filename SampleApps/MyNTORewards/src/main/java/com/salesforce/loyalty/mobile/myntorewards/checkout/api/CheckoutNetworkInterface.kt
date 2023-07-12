package com.salesforce.loyalty.mobile.myntorewards.checkout.api

import com.salesforce.loyalty.mobile.myntorewards.checkout.models.*
import retrofit2.http.*

interface CheckoutNetworkInterface {

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