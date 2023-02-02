package com.salesforce.loyalty.mobile.sources.loyaltyExtensions

import java.util.UUID

/**
 * Class which exposes Loyalty utility methods
 */
object LoyaltyUtils {

    /**
     * Returns a randomly generated string
     *
     * @return Random String
     */
    fun generateRandomString(): String {
        return UUID.randomUUID().toString()
    }
}