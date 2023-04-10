package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

/**
 * ForceConfig class is a utility class for holding constant values.
 */
object ForceConfig {
    const val ACCESS_TOKEN_BASE_URL_OLD = "https://login.salesforce.com/services/oauth2/"
    const val ACCESS_TOKEN_BASE_URL = "https://na45.test1.pc-rnd.salesforce.com/services/oauth2/"
    const val GRANT_TYPE_PASSWORD = "password"
    const val ADMIN_USERNAME = "archit.sharma@salesforce.com"
    const val ADMIN_PASSWORD = "test@321"
    const val CONSUMER_SECRET = "4C64F15ECE02FFF0002BA74DFDE835A94FE05C7DC1DA3096604E634701E844AA"
    const val CONSUMER_KEY =
        "3MVG9sA57VMGPDff5IP2PZ3gePzAE087y65OQNiwULLemkJnFilih4d4Ttixw0abfb8XH__8miW3Xn9yStqlg"
    const val HEADER_AUTHORIZATION = "Authorization"

    object MimeType {
        const val JSON = "application/json;charset=UTF-8"
        const val FORM_ENCODED = "application/x-www-form-urlencoded;charset=utf-8"
    }
}