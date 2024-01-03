/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.referral_sdk

/**
 * Enum class that holds values of the frequency at which transaction journal statements must be delivered to the member.
 */
enum class TransactionalJournalStatementFrequency(val frequency: String) {
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly")
}