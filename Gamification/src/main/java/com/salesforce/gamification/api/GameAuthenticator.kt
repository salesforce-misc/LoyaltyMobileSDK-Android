/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.gamification.api

/**
 * An interface that defines the necessary methods for handling access tokens in the Salesforce API.
 */
interface GameAuthenticator {

    /**
     * Get a valid access token.
     *
     * @return A valid access token as a String if available, otherwise null.
     */
    fun getAccessToken(): String?

    /**
     * Grant (or refresh) an access token.
     *
     * @return A refreshed or newly granted access token as a String.
     */
    suspend fun grantAccessToken(): String?

}