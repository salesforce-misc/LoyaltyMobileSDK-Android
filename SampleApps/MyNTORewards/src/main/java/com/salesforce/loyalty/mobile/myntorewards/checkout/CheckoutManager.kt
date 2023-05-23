package com.salesforce.loyalty.mobile.myntorewards.checkout

import android.util.Log
import com.salesforce.loyalty.mobile.myntorewards.checkout.api.CheckoutConfig
import com.salesforce.loyalty.mobile.myntorewards.checkout.api.CheckoutNetworkClient
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.*
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator


class CheckoutManager constructor(auth: ForceAuthenticator, instanceUrl: String){

    companion object {
        private const val TAG = "CheckoutManager"
    }

    private val authenticator: ForceAuthenticator

    private val checkoutClient: CheckoutNetworkClient

    private val mInstanceUrl: String

    init {
        authenticator = auth
        mInstanceUrl = instanceUrl
        checkoutClient = CheckoutNetworkClient(auth, instanceUrl)
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

        return checkoutClient.authApi.createOrder(
            getOrderCreationUrl(),
            body
        )
    }

    suspend fun getShippingMethods(
    ): Result<List<ShippingMethod>> {
        Log.d(TAG, "getShippingMethods()")

        return checkoutClient.authApi.getShippingMethods(
            getShippingMethodsUrl()
        )
    }

    suspend fun getOrderDetails(
        orderId: String
    ): Result<OrderDetailsResponse> {
        Log.d(TAG, "getOrderDetails()")

        return checkoutClient.authApi.getOrderDetails(
            getOrderCreationUrl(), orderId = orderId
        )
    }

    suspend fun getShippingBillingAddressSOQL(
    ): ShippingBillingAddressRecord? {
        Log.d(TAG, "getShippingBillingAddressSOQL()")

        val result = checkoutClient.authApi.getShippingBillingAddressSOQL(
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
        return mInstanceUrl + CheckoutConfig.CHECKOUT_ORDER_CREATION + "/"
    }

    private fun getShippingMethodsUrl(): String {
        return mInstanceUrl + CheckoutConfig.CHECKOUT_SHIPPING_METHODS + "/"
    }

    private fun getShippingBillingSOQLUrl(): String {
        return mInstanceUrl + CheckoutConfig.SOQL_QUERY_PATH + CheckoutConfig.SOQL_QUERY_VERSION + CheckoutConfig.QUERY
    }

    private fun getShippingBillingAddressSOQLQuery(): String {
        return "select shippingAddress, billingAddress from account"
    }
}