package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

/**
 * ForceConfig class is a utility class for holding constant values.
 */
object ForceConfig {
    const val ACCESS_TOKEN_BASE_URL_OLD = "https://login.salesforce.com/"
    const val ACCESS_TOKEN_BASE_URL = "https://na45.test1.pc-rnd.salesforce.com"
    const val HEADER_AUTH_REQUEST_TYPE_VALUE = "Named-User"
    const val AUTHORIZATION_GRANT_TYPE = "authorization_code"
    const val REFRESH_GRANT_TYPE = "refresh_token"
    const val AUTHORIZATION_REQ_PATH = "/services/oauth2/authorize"
    const val ACCESS_TOKEN_REQ_PATH = "/services/oauth2/token"
    const val REVOKE_TOKEN_REQ_PATH = "/services/oauth2/revoke"
    const val SOQL_QUERY_PATH = "/services/data/v"
    const val SOQL_QUERY_VERSION = "58.0"
    const val QUERY = "/query/"
    const val HTTP_RESPONSE_CODE_SERVER_ERROR = 500

    object MimeType {
        const val JSON = "application/json;charset=UTF-8"
        const val FORM_ENCODED = "application/x-www-form-urlencoded;charset=utf-8"
    }

    fun getAuthorizationCodeRequestUrl(communityUrl: String): String {
        return communityUrl + AUTHORIZATION_REQ_PATH
    }

    fun getAccessTokenRequestUrl(communityUrl: String): String {
        return communityUrl + ACCESS_TOKEN_REQ_PATH
    }

    fun getRefreshAccessTokenRequestUrl(baseUrl: String): String {
        return baseUrl + ACCESS_TOKEN_REQ_PATH
    }

    fun getRevokeAccessTokenRequestUrl(communityUrl: String): String {
        return communityUrl + REVOKE_TOKEN_REQ_PATH
    }
}