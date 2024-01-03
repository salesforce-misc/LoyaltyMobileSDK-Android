/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.referral_sdk

/**
 * Enum that holds the values of channels used by loyalty program member to enroll in to the loyalty program.
 */
enum class EnrollmentChannel(val channel: String) {
    POS("Pos"),
    WEB("Web"),
    EMAIL("Email"),
    CALL_CENTER("CallCenter"),
    SOCIAL("Social"),
    MOBILE("Mobile"),
    STORE("Store"),
    FRANCHISE("Franchise"),
    PARTNER("Partner"),
    PRINT("Print")
}