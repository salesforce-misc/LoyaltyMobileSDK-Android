/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.forceUtils

/**
 * ForceError class is an enum class that holds various error cases.
 */
enum class ForceError(val customDescription: String) {
    REQUEST_FAILED("Request Failed Error"),
    JSON_CONVERSION_FAILURE("JSON Conversion Failure"),
    INVALID_DATA("Invalid Data Error"),
    RESPONSE_UNSUCCESSFUL("Response Unsuccessful Error"),
    JSON_PARSING_FAILURE("JSON Parsing Failure Error"),
    NO_INTERNET("No internet connection"),
    FAILED_SERIALIZATION("Serialization print for debug failed."),
    AUTHENTICATION_NEEDED("Authentication needed"),
    USER_IDENTITY_NEEDED("User Identity has not been set."),
    AUTH_NOT_FOUND_IN_KEYCHAIN("Cannot find the auth from Keychain.")
}