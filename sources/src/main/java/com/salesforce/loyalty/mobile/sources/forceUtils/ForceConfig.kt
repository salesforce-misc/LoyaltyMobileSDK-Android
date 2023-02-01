package com.salesforce.loyalty.mobile.sources.forceUtils

/**
 * ForceConfig class is a utility class for holding constant values.
 */
object ForceConfig {
    const val ACCESS_TOKEN_BASE_URL = "https://login.salesforce.com/services/oauth2/"
    const val GRANT_TYPE_PASSWORD = "password"
    const val ADMIN_USERNAME = "admin_aa_22852@loyaltysampleapp.com"
    const val ADMIN_PASSWORD = "50Fremont"
    const val CONSUMER_SECRET = "F89D25E00C8295DBDD0F8AD08750E69633B6E46B6515AA8BED7D30CDEEA2BD5E"
    const val CONSUMER_KEY =
        "3MVG9kBt168mda_.AhACC.RZAxIT77sS1y2_ltn1YMi4tG98ZA1nMiP2w6m51xen0Of_0TWZ8RTvPy05bK5p9"
    const val HEADER_AUTHORIZATION = "Authorization"

    object MimeType {
        const val JSON = "application/json;charset=UTF-8"
        const val FORM_ENCODED = "application/x-www-form-urlencoded;charset=utf-8"
    }
}