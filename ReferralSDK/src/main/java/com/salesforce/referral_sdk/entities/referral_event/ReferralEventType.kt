/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.referral_sdk.entities.referral_event

/**
 * Enum that holds the Enrollment status of an Advocate to the Referral Promotion
 */
enum class ReferralEventType(val eventType: String) {
    ENROLLMENT("Enrollment"),
    PURCHASE("Purchase"),
    REFER("Refer")
}