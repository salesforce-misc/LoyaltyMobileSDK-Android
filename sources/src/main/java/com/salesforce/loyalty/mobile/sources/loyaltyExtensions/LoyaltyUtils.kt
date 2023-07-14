/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyExtensions

import java.util.UUID

/**
 * Class which exposes Loyalty utility methods
 */
object LoyaltyUtils {

    /**
     * Returns a randomly generated string
     *
     * @param length Length of random string needed
     * @return Random String
     */

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}