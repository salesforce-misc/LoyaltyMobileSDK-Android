package com.salesforce.loyalty.mobile.myntorewards.checkout.api

object CheckoutConfig {

    const val HEADER_AUTHORIZATION = "Authorization"
    const val CHECKOUT_ORDER_CREATION = "/services/apexrest/NTOOrderCheckOut"
    const val CHECKOUT_SHIPPING_METHODS = "/services/apexrest/ShippingMethods"
    const val CHECKOUT_ORDER_CREATION_PARTICIPANT_REWARD = "/services/apexrest/NTOOrderCheckOutAndGameParticipantReward"
    const val CHECKOUT_ORDER_ID = "orderId"

    const val SOQL_QUERY_PATH = "/services/data/v"
    const val SOQL_QUERY_VERSION = "56.0"
    const val QUERY = "/query/"

    object MimeType {
        const val JSON = "application/json;charset=UTF-8"
        const val FORM_ENCODED = "application/x-www-form-urlencoded;charset=utf-8"
    }
}