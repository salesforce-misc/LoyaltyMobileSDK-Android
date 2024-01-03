/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.referral_sdk

/**
 * Enum class that holds the status of the loyalty program member.
 */
enum class MemberStatus(val status: String) {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    CUSTOM("Custom")
}