/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.referral_sdk

/**
 * Enum class that holds values of the method used to deliver transaction journal statements to the member.
 */
enum class TransactionalJournalStatementMethod(val method: String) {
    MAIL("Mail"),
    EMAIL("Email")
}