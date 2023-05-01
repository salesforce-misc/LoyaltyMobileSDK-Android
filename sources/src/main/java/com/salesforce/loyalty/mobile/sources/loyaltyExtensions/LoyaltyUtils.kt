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
     * @return Random String
     */
    fun generateRandomString(): String {
        return UUID.randomUUID().toString()
    }
}