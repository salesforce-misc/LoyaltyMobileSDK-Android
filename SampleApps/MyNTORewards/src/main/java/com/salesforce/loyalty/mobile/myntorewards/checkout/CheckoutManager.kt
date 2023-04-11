package com.salesforce.loyalty.mobile.myntorewards.checkout

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.salesforce.loyalty.mobile.myntorewards.checkout.api.CheckoutAuth
import com.salesforce.loyalty.mobile.myntorewards.checkout.api.CheckoutConfig
import com.salesforce.loyalty.mobile.myntorewards.checkout.api.CheckoutNetworkClient
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.*
import com.salesforce.loyalty.mobile.sources.forceModels.ForceAuthResponse


object CheckoutManager {

    private const val TAG = "CheckoutManager"

    suspend fun getAccessToken(): Result<ForceAuthResponse> {
        Log.d(TAG, "getAccessToken()")
        val response = CheckoutNetworkClient.authApi.getAccessToken(
            CheckoutConfig.MimeType.JSON,
            CheckoutConfig.MimeType.FORM_ENCODED,
            CheckoutConfig.ADMIN_USERNAME,
            CheckoutConfig.ADMIN_PASSWORD,
            CheckoutConfig.GRANT_TYPE_PASSWORD,
            CheckoutConfig.CONSUMER_KEY,
            CheckoutConfig.CONSUMER_SECRET
        )
        response.onSuccess {
            CheckoutAuth.setAuthToken(it.accessToken)
        }
        return response
    }

    suspend fun createOrder(
    ): Result<String> {
        Log.d(TAG, "createOrder()")

        val body = OrderCreationRequest(
            productName = "Men's Rainier L4 Windproof Soft Shell Hoodie",
            redeemPoints = 0,
            productPrice = 200,
            orderTotal = 207,
            useNTOPoints = false,
            pointsBalance = 250,
            shippingStreet = "1520 W 62nd St Cook IL 60636",
            billingStreet = "1520 W 62nd St Cook IL 60636",
            voucherCode = "",
            membershipNumber = "24345671"
        )

        return CheckoutNetworkClient.authApi.createOrder(
            getOrderCreationUrl(),
            body
        )
    }

    suspend fun getShippingMethods(
    ): Result<List<ShippingMethod>> {
        Log.d(TAG, "getShippingMethods()")

        return CheckoutNetworkClient.authApi.getShippingMethods(
            getShippingMethodsUrl()
        )
    }

    suspend fun getOrderDetails(
        orderId: String
    ): Result<OrderDetailsResponse> {
        Log.d(TAG, "getOrderDetails()")

        return CheckoutNetworkClient.authApi.getOrderDetails(
            getOrderCreationUrl(), orderId = orderId
        )
    }

    suspend fun getShippingBillingAddressSOQL(
    ): ShippingBillingAddressRecord? {
        Log.d(TAG, "getShippingBillingAddressSOQL()")

        val result = CheckoutNetworkClient.authApi.getShippingBillingAddressSOQL(
            getShippingBillingSOQLUrl(), getShippingBillingAddressSOQLQuery()
        )
        result.onSuccess { queryResult: QueryResult<ShippingBillingAddressRecord> ->
            queryResult?.records?.let {
                if (!it.isNullOrEmpty()) {
                    return it[0]
                }
            }
        }
        return null
    }

    private fun getOrderCreationUrl(): String {
        return CheckoutConfig.MEMBER_BASE_URL + CheckoutConfig.CHECKOUT_ORDER_CREATION + "/"
    }

    private fun getShippingMethodsUrl(): String {
        return CheckoutConfig.MEMBER_BASE_URL + CheckoutConfig.CHECKOUT_SHIPPING_METHODS + "/"
    }

    private fun getShippingBillingSOQLUrl(): String {
        return CheckoutConfig.MEMBER_BASE_URL + CheckoutConfig.SOQL_QUERY_PATH + CheckoutConfig.SOQL_QUERY_VERSION + CheckoutConfig.QUERY
    }

    private fun getShippingBillingAddressSOQLQuery() : String{
        return "select shippingAddress, billingAddress from account"
    }
}